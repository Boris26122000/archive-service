# Archive Service API


## If we add multiple archiving methods (7z, for example)

To add additional archive types we need:
- add needed type to [ArchiveType.java](archive-domain%2Fsrc%2Fmain%2Fjava%2Fio%2Farchiveservice%2Fenums%2FArchiveType.java)
- introduce new parameter in .../archive endpoint with archive type
- create new implementation of [ArchiveService.java](archive-application%2Fsrc%2Fmain%2Fjava%2Fio%2Farchiveservice%2Fservice%2FArchiveService.java) with name in @Service annotation
- add name for existing zip archive implementation in @Service annotation
- inject ArchiveService with implementation for 7z using @Qualifier annotation in [ArchiveServiceFacade.java](archive-application%2Fsrc%2Fmain%2Fjava%2Fio%2Farchiveservice%2Fservice%2Fimpl%2FArchiveServiceFacade.java)  
- update method "archive" in ArchiveServiceFacade to support different types of archive. Also use zip by default, if type is not specified in request

## Face a significant increase in request count

To handle significant increase in request we need:
- environment improvements:
  - think of adding more instances of service to handle the increased number of requests
  - add load balancer with appropriate algorithm to distribute incoming requests across multiple instances
- Java improvements:
  - create a configuration class to define a ThreadPoolTaskExecutor bean that will handle the asynchronous tasks 
  - configure new Bean
  - add @Async("BeanName") annotation on ArchiveServiceFacade

## Allow 1GB max file size

To add additional archive types we need:
- update properties: 'archive.zip.max-upload-size' and 'spring.servlet.multipart.max-file-size'
- update archive logic:
  - while iterating through MultipartFile[] array, start InputStream of file.
  - read file bytes from stream by small parts (instead of direct reading all file's bytes and writing to zip)
  - f.e. part of reading by 1024 bytes per part from file's input stream 
``` java
			InputStream fis = file.getInputStream();
			ZipEntry zipEntry = new ZipEntry(file.getOriginalFilename());
			zos.putNextEntry(zipEntry);

			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zos.write(bytes, 0, length);
			}

			zos.closeEntry();
			fis.close();    
```

