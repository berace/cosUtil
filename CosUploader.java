ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
		if (isAjaxUpload) {
			upload.setHeaderEncoding("UTF-8");
		}
    
    List<FileItem> fileItems = upload.parseRequest(request);
			for (FileItem ft : fileItems) {
				if (!ft.isFormField()) {
					fileItem = ft;
					break;
				}
			}
      
      InputStream is = fileItem.getInputStream();
			long size = fileItem.getSize();
			COSClient cosclient = CosClientUtil.getInstance();
		ObjectMetadata objectMetadata = new ObjectMetadata();
		// 从输入流上传必须制定content length, 否则http客户端可能会缓存所有数据，存在内存OOM的情况
		objectMetadata.setContentLength(contentSize);
		// 默认下载时根据cos路径key的后缀返回响应的contenttype, 上传时设置contenttype会覆盖默认值
		objectMetadata.setContentType(contentType);
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, is, objectMetadata);
		// 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
		putObjectRequest.setStorageClass(StorageClass.Standard_IA);
		try {
			cosclient.putObject(putObjectRequest);
			state = new BaseState(true);
		} catch (CosServiceException e) {
			e.printStackTrace();
			return new BaseState(false, AppInfo.COS_SERVICE_ERROR);
		} catch (CosClientException e) {
			e.printStackTrace();
			return new BaseState(false, AppInfo.COS_CLIENT_ERROR);
		} finally {
			// 关闭客户端
			cosclient.shutdown();
		}
