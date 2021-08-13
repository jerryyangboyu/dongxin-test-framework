package pro.boyu.dongxin.framework;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PackageUtility {
    private static boolean recursive = false;

    private static String[] excludeClasses;

    public static void setExcludeClasses(String[] classes) {
        excludeClasses = classes;
    }

    public static void setRecursive(boolean opts) {
        PackageUtility.recursive = opts;
    }

    public static Set<Class<?>> getClasses(String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        String packagePath = packageName.replace('.', File.separatorChar);
        try {
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packagePath);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filepath = URLDecoder.decode(url.getFile(), String.valueOf(StandardCharsets.UTF_8));
                    iterateDirectory(packageName, filepath, classes, PackageUtility.recursive);
                } else if ("jar".equals(protocol)) {
                    JarFile jar;
                    try {
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx)
                                            .replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class")
                                            && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(
                                                packageName.length() + 1, name
                                                        .length() - 6);
                                        try {
                                            // 添加到classes
                                            classes.add(Class
                                                    .forName(packageName + '.'
                                                            + className));
                                        } catch (ClassNotFoundException e) {
                                            System.out.println("Cannot load class " + className);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        // log.error("在扫描用户定义视图时从jar包获取文件出错");
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return classes;
    }

    private static void iterateDirectory(String packageName, String packagePath, Set<Class<?>> classes, boolean recursive) {
        // collect all files
        File dirs = new File(packagePath);
        if (!dirs.exists() || !dirs.isDirectory()) return;
        File[] files = dirs.listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                // dynamic load .class file and directory during runtime
                return (recursive && f.isDirectory()) || f.getName().endsWith(".class");
            }
        });

        // iterate all files
        for (File file: files) {
            if (file.isDirectory()) {
                iterateDirectory(String.format("%s.%s", packageName, file.getName()),
                        file.getAbsolutePath(),
                        classes,
                        true);
            } else {
                String classname = file.getName().substring(0, file.getName().length() - 6);

                if (excludeClasses != null) {
                    boolean duplicate = false;
                    for (String s: excludeClasses) {
                        if (classname.equals(s)) {
                            duplicate = true;
                            break;
                        };
                    }
                    if (duplicate) break;
                }

                try {
                    Class<?> c = Thread.currentThread().getContextClassLoader().loadClass(String.format("%s.%s", packageName, classname));
                    classes.add(c);
                } catch (ClassNotFoundException e) {
                    System.out.println("ERROR: Class " + classname + " not found.");
                }
            }
        }
    }
}
