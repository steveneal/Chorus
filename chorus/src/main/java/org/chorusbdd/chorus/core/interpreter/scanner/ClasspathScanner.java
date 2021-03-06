/**
 *  Copyright (C) 2000-2012 The Software Conservancy as Trustee.
 *  All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to
 *  deal in the Software without restriction, including without limitation the
 *  rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 *  sell copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *  IN THE SOFTWARE.
 *
 *  Nothing in this notice shall be deemed to grant any rights to trademarks,
 *  copyrights, patents, trade secrets or any other intellectual property of the
 *  licensor or any contributor except as expressly stated herein. No patent
 *  license is granted separate from the Software, for code that you delete from
 *  the Software, or for combinations of the Software with other software or
 *  hardware.
 */
package org.chorusbdd.chorus.core.interpreter.scanner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * You are free to use this code however you
 * like but I assume no responsibility for it's
 * correctness and provide no warranty of any kind.
 * <p/>
 * Code to find files on your classpath.
 * Have you ever wanted to get a list of the files on your classpath or perhaps
 * get a list of the classes on your classpath? It's not exactly trivial.
 * You have to parse the classpath, root through the jar files, and root through
 * the directories. Here is some code that will enable you to do just that and
 * it also allows you to provide a filter so that you can find all of the
 * jpeg files for example.
 *
 * @author Michael Connor
 * @author Klaus Berg
 * @author Nick Ebbutt, added ClassFilter
 */
public class ClasspathScanner {

    public static Set<Class> doScan(String... packageName) throws IOException, ClassNotFoundException {
        return doScan(ClassFilter.NULL_FILTER, packageName);
    }

    public static Set<Class> doScan(ClassFilter classFilter, String... packageName) throws ClassNotFoundException, IOException {
        Set<Class> s = new HashSet<Class>();
        for (String p : packageName) {
            s.addAll(doScan(classFilter, p));
        }
        return s;
    }

    public static Set<Class> doScan(ClassFilter classFilter, String packageName) throws IOException, ClassNotFoundException {
        Set<Class> s = new HashSet<Class>();
        for (String clazz : getClasspathClassNames()) {
            if (clazz.startsWith(packageName)) {

                Class c = Class.forName(clazz);
                if (classFilter.accept(c)) {
                    s.add(c);
                }
            }
        }
        return s;
    }

    /**
     * Returns a list of the classes on the
     * classpath. The names returned will
     * be appropriate for using Class.forName(String)
     * in that the slashes will be changed to dots
     * and the .class file extension will be removed.
     */
    public static String[] getClasspathClassNames()
            throws ZipException, IOException {
        final String[] classes = getClasspathFileNamesWithExtension(".class");
        for (int i = 0; i < classes.length; i++) {
            classes[i] = classes[i].substring(0, classes[i].length() - 6).replace("/", ".");
        }
        return classes;
    }

    public static String[] getClasspathFileNamesWithExtension(final String extension) throws ZipException, IOException {
        return getClasspathFileNames(new FilenameFilter() {
            public boolean accept(String filename) {
                return filename.endsWith(extension);
            }
        });
    }

    public static String[] getClasspathFileNames(FilenameFilter filter)
            throws ZipException, IOException {
        final List<String> filenames = new ArrayList<String>();
        for (String filename : getClasspathFileNames()) {
            if (filter.accept(filename)) {
                filenames.add(filename);
            }
        }
        return filenames.toArray(new String[filenames.size()]);
    }

    /**
     * Returns the fully qualified class names of
     * all the classes in the classpath. Checks
     * directories and zip files. The FilenameFilter
     * will be applied only to files that are in the
     * zip files and the directories. In other words,
     * the filter will not be used to sort directories.
     */
    public static String[] getClasspathFileNames() throws ZipException, IOException {
        final StringTokenizer tokenizer = new StringTokenizer(System.getProperty("java.class.path"), File.pathSeparator, false);
        final Set<String> filenames = new LinkedHashSet<String>();

        while (tokenizer.hasMoreTokens()) {
            final String classpathElement = tokenizer.nextToken();
            final File classpathFile = new File(classpathElement);

            if (classpathFile.exists() && classpathFile.canRead()) {
                if (classpathElement.toLowerCase(Locale.US).endsWith(".jar")) {
                    final ZipFile zip = new ZipFile(classpathFile);
                    Enumeration<? extends ZipEntry> entries = zip.entries();

                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();
                        if (!entry.isDirectory()) {
                            filenames.add(entry.getName());
                        }
                    }

                } else if (classpathFile.isDirectory()) {
                    // lets go through and find all of the subfolders
                    final Set<File> directoriesToSearch = new HashSet<File>();
                    final Set<File> newDirectories = new HashSet<File>();
                    directoriesToSearch.add(classpathFile);
                    final String basePath = classpathFile.getAbsolutePath();

                    while (directoriesToSearch.size() > 0) {
                        for (File searchDirectory : directoriesToSearch) {
                            File[] directoryFiles = searchDirectory.listFiles();
                            for (File directoryFile : directoryFiles) {
                                if (directoryFile.isDirectory()) {
                                    newDirectories.add(directoryFile);
                                } else {
                                    filenames.add(directoryFile.getAbsolutePath().substring(basePath.length() + 1));
                                }
                            }
                        }
                        directoriesToSearch.clear();
                        directoriesToSearch.addAll(newDirectories);
                        newDirectories.clear();
                    }
                }
            }
        }

        final String[] uniqueNames = new String[filenames.size()];
        int index = 0;

        for (String name : filenames) {
            uniqueNames[index++] = name.replace("\\", "/");
        }

        return uniqueNames;
    }

    public static void main(String[] args) throws Exception {
        final String[] names = getClasspathClassNames();
        final BufferedWriter writer = new BufferedWriter(new FileWriter("allClasses.txt"));
        for (String name : names) {
            writer.write(name);
            writer.write("\n");
        }
        writer.close();
        System.out.println("All done.");
    }
}