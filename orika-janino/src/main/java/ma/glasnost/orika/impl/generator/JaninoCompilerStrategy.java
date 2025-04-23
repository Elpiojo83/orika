/*
 * Orika - simpler, better and faster Java bean mapping
 *
 *  Copyright (C) 2011-2019 Orika authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package ma.glasnost.orika.impl.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.AbstractSet;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;

import ma.glasnost.orika.MappingException;
import ma.glasnost.orika.util.WeakList;

import org.codehaus.janino.ClassLoaderIClassLoader;
import org.codehaus.janino.IClassLoader;
import org.codehaus.janino.Java;
import org.codehaus.janino.Parser;
import org.codehaus.janino.Scanner;
import org.codehaus.janino.UnitCompiler;
import org.codehaus.janino.util.ClassFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaninoCompilerStrategy extends CompilerStrategy {

  private static final Logger LOG = LoggerFactory.getLogger(JaninoCompilerStrategy.class);

  private static final String WRITE_SOURCE_FILES_BY_DEFAULT = "false";
  private static final String WRITE_CLASS_FILES_BY_DEFAULT = "false";

  private final ClassLoader parentClassLoader;
  private final IClassLoader iClassLoader;
  private final AggregatedClassLoader aggregatedClassLoader;

  public JaninoCompilerStrategy() {
    super(WRITE_SOURCE_FILES_BY_DEFAULT, WRITE_CLASS_FILES_BY_DEFAULT);
    parentClassLoader = Thread.currentThread().getContextClassLoader();
    aggregatedClassLoader = new AggregatedClassLoader(parentClassLoader);
    iClassLoader = new ClassLoaderIClassLoader(aggregatedClassLoader);
  }

  /**
   * A java.lang.IllegalAccessException is thrown when one attempts to access a method or member
   * that visibility qualifiers do not allow. Typical examples are attempting to access private or
   * protected methods or instance variables. Another common example is accessing package protected
   * methods or members from a class that appears to be in the correct package, but is really not
   * due to caller and callee classes being loaded by different class loaders.
   *
   * <p>Loads a class file by {@code java.lang.invoke.MethodHandles.Lookup}. It is obtained by using
   * {@code neighbor}.
   *
   * @param neighbor
   * @param bcode byte array (bytecode)
   * @return
   */
  public static Class<?> toClass(Class<?> neighbor, byte[] bcode) {
    try {
      JaninoCompilerStrategy.class.getModule().addReads(neighbor.getModule());
      MethodHandles.Lookup lookup = MethodHandles.lookup();
      MethodHandles.Lookup prvlookup = MethodHandles.privateLookupIn(neighbor, lookup);
      return prvlookup.defineClass(bcode);
    } catch (IllegalAccessException | IllegalArgumentException e) {
      throw new MappingException(
          e.getMessage() + ": " + neighbor.getName() + " has no permission to define the class", e);
    }
  }

  @Override
  public Class<?> compileClass(SourceCodeContext sourceCode) throws SourceCodeGenerationException {
    Scanner scanner;
    try {
      writeSourceFile(sourceCode);

      scanner = new Scanner(sourceCode.getClassName(), new StringReader(sourceCode.toSourceFile()));
      Java.CompilationUnit localCompilationUnit = new Parser(scanner).parseCompilationUnit();
      UnitCompiler unitCompile = new UnitCompiler(localCompilationUnit, iClassLoader);
      ClassFile[] classes = unitCompile.compileUnit(true, true, true);

      Class<?> neighbor = sourceCode.getNeighbor();

      if (neighbor == null) {
        return aggregatedClassLoader.defineClass(
            classes[0].getThisClassName(), classes[0].toByteArray());
      }
      if (neighbor.isArray()) {
        neighbor = neighbor.getComponentType();
      }
      return toClass(neighbor, classes[0].toByteArray());
    } catch (Exception e) {
      LOG.error("Can not compile {0}", sourceCode.getClassName(), e);
      LOG.debug(sourceCode.toSourceFile());
      throw new MappingException("Can not compile the generated mapper", e);
    }
  }

  /**
   * Attempts to register a class-loader in the maintained list of referenced class-loaders. Returns
   * true if the class-loader was registered as a result of the call; false is returned if the
   * class-loader was already registered.
   *
   * @param cl
   * @return true if the class-loader was registered as a result of this call; false if the
   *     class-loader was already registered
   */
  private boolean registerClassLoader(ClassLoader cl) {
    return aggregatedClassLoader.addClassLoader(cl);
  }

  public void assureTypeIsAccessible(Class<?> type) throws SourceCodeGenerationException {
    if (!type.isPrimitive()) {
      Analysis.Visibility visibility = Analysis.getMostRestrictiveVisibility(type);
      if (visibility == Analysis.Visibility.PRIVATE) {
        throw new SourceCodeGenerationException(type + " is not accessible");
      }

      String className = type.getName();
      if (type.isArray()) {
        // Strip off the "[L" prefix from the internal name
        className = type.getComponentType().getName();
      }
      if (type.getClassLoader() != null) {
        try {
          aggregatedClassLoader.findClass(className);
        } catch (ClassNotFoundException e) {
          if (registerClassLoader(type.getClassLoader())) {
            try {
              aggregatedClassLoader.findClass(className);
            } catch (ClassNotFoundException e2) {
              throw new SourceCodeGenerationException(type + " is not accessible", e2);
            }
          } else {
            throw new SourceCodeGenerationException(type + " is not accessible", e);
          }
        }
      }
    }
  }

  /**
   * Produces the requested source file for debugging purposes.
   *
   * @throws IOException
   */
  protected void writeSourceFile(SourceCodeContext sourceCode) throws IOException {
    if (writeSourceFiles) {
      File parentDir = preparePackageOutputPath(this.pathToWriteSourceFiles, sourceCode.getPackageName());
      File sourceFile = new File(parentDir, sourceCode.getClassSimpleName() + ".java");
      if (!sourceFile.exists() && !sourceFile.createNewFile()) {
        throw new IOException("Could not write source file for " + sourceCode.getClassName());
      }

      try (FileWriter fw = new FileWriter(sourceFile)) {
        fw.append(sourceCode.toSourceFile());
        LOG.debug("Source file written to {}", sourceFile);
      }
    }
  }

  private static class AggregatedClassLoader extends ClassLoader {

    private final SafeClassLoaderSet individualClassLoaders;

    private AggregatedClassLoader(ClassLoader parent) {
      super(parent);
      individualClassLoaders = new SafeClassLoaderSet();
      individualClassLoaders.add(parent);
    }

    public boolean addClassLoader(ClassLoader classLoader) {
      classLoader.hashCode();
      synchronized (this) {
        if (classLoader == null || individualClassLoaders == null) {
          return false;
        }
        return individualClassLoaders.add(classLoader);
      }
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
      final LinkedHashSet<URL> resourceUrls = new LinkedHashSet<>();

      for (ClassLoader classLoader : individualClassLoaders) {
        final Enumeration<URL> urls = classLoader.getResources(name);
        while (urls.hasMoreElements()) {
          resourceUrls.add(urls.nextElement());
        }
      }

      return new Enumeration<>() {
        final Iterator<URL> resourceUrlIterator = resourceUrls.iterator();

        @Override
        public boolean hasMoreElements() {
          return resourceUrlIterator.hasNext();
        }

        @Override
        public URL nextElement() {
          return resourceUrlIterator.next();
        }
      };
    }

    @Override
    protected URL findResource(String name) {
      for (ClassLoader classLoader : individualClassLoaders) {
        final URL resource = classLoader.getResource(name);
        if (resource != null) {
          return resource;
        }
      }
      return super.findResource(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
      for (ClassLoader classLoader : individualClassLoaders) {
        try {
          return classLoader.loadClass(name);
        } catch (Exception ignore) {
        } catch (LinkageError ignore) {
        }
      }

      return super.findClass(name);
    }

    public Class<?> defineClass(String name, byte[] b) {
      return defineClass(name, b, 0, b.length);
    }
  }

  static class SafeClassLoaderSet extends AbstractSet<ClassLoader> {

    WeakList<ClassLoader> arrayList = new WeakList<>();

    @Override
    public boolean add(ClassLoader classLoader) {
      if (arrayList.has(classLoader)) {
        return false;
      }
      arrayList.add(classLoader);
      return false;
    }

    @Override
    public Iterator<ClassLoader> iterator() {
      return new WeakListIterator();
    }

    @Override
    public int size() {
      return arrayList.size();
    }

    private class WeakListIterator implements Iterator<ClassLoader> {

      private int n;
      private int i;

      public WeakListIterator() {
        n = size();
        i = 0;
      }

      public boolean hasNext() {
        return i < n;
      }

      public ClassLoader next() {
        return arrayList.get(i++);
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }
    }
  }


}
