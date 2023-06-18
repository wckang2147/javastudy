String   strTemp   = "mainClass";
File jarFile    = new File( "jar file path" );
URL[] urls = new URL[]{ new URL( "jar:" + file.toURL() + "!/" ) };
ClassLoader    classLoader = new URLClassLoader( urls );
Class    tempClass    = classLoader.loadClass( temp.mainClass );        // plugin class load
Object    object    = tempClass.newInstance();
Method    method    = tempClass.getMethod( "optionSet",
                                                            new Class[]{
                                                                    strTemp.getClass()
                                                            } );
method.invoke( object, strTemp );



// Loading Class
File jarFile    = new File( "class package path" );      // 파일명이 아닌 폴더면만 기제
URL[] urls = new URL[]{ new URL( file.toURL() ) };

// Get current abs path
public static void main(String args[]) {
  String currentPaht = Paths.get("").toAbsolutePath().toString();
}
public static void main(String args[]) {
  String currentPath = System.getProperty("user.dir");
}
public static void main(String args[]) {
  String currentPath = 
          FileSystems.getDefault().getPath("").toAbsolutePath().toString();
}
public static void main(String args[]) {
  String currentPath = new File("").getAbsolutePath();
}


// 작업 디렉토리를 절대경로로 변환
public static void main(String args[]) {
  String currentPath = 
          FileSystems.getDefault().getPath("").toAbsolutePath().toString();
}
public static void main(String args[]) {
  String currentPath = System.getProperty("user.dir");
}
public static void main(String args[]) {
  String currentPaht = Paths.get("").toAbsolutePath().toString();
}

