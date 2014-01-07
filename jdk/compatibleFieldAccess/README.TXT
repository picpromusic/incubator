Place asm-debug-all-4.2.jar in libs dir and run
ant clean
ant test-enabled

you can download the jar from here:
http://forge.ow2.org/project/showfiles.php?group_id=23

The output should end with:

test-enabled:
     [echo] OLD(enabled)
     [java] ByteCodeTransformation 1=false 2=false Bootstrap=false
     [java] Listening for transport dt_socket at address: 8000
     [java] Everything is fine
     [echo] NEW(enabled)
     [java] ByteCodeTransformation 1=false 2=false Bootstrap=false
     [java] Listening for transport dt_socket at address: 8000
     [java] TestConfiguration: noReflectionPossible. Skip ReflectionTest
     [java] Everything is fine
     [echo] NEW2(enabled)
     [java] ByteCodeTransformation 1=false 2=false Bootstrap=false
     [java] Listening for transport dt_socket at address: 8000
     [java] TestConfiguration: noReflectionPossible. Skip ReflectionTest
     [java] Everything is fine
     [echo] NEW3(enabled)
     [java] ByteCodeTransformation 1=false 2=false Bootstrap=false
     [java] Listening for transport dt_socket at address: 8000
     [java] TestConfiguration: noReflectionPossible. Skip ReflectionTest
     [java] Everything is fine
BUILD SUCCESSFUL