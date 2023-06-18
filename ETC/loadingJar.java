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
