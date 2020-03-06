public class CosClientUtil {

	private static String secretId = null;
	private static String secretKey = null;
	/**
	 * 再次设置了bucketName，则所有cosclient操作都是同一个bucketName 会覆盖config。json里的cosBucketName的值
	 * 此处设计是为了，不同模块的文件可能存储不同的bucket中，这样可以在各自的config。json中设置。
	 */
	public static String bucketName = null;

	private static String region = "ap-beijing";

	static enum SingletonEnum {

		INSTANCE;

		private COSClient cosclient;

		private SingletonEnum() {
			// 1 初始化用户身份信息(secretId, secretKey)
			COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
			// 2 设置bucket的区域, COS地域的简称请参照 https://www.qcloud.com/document/product/436/6224
			ClientConfig clientConfig = new ClientConfig(new Region(region));
			// 3 生成cos客户端
			cosclient = new COSClient(cred, clientConfig);
		}

		public COSClient getInstnce() {
			return cosclient;
		}
	}

	/**
	 * 获取 cos 客户端。前提是通过setConfig()设置好相应参数
	 * @return
	 */
	public static COSClient getInstance() {
		return SingletonEnum.INSTANCE.getInstnce();
	}

	/**
	 * 
	 * @param secretId your accessKey(secretId). you can get it by https://console.qcloud.com/capi
	 * @param secretKey your accessKey(secretId). you can get it by https://console.qcloud.com/capi
	 */
	public static void setConfig(String secretId, String secretKey) {
		CosClientUtil.secretId = secretId;
		CosClientUtil.secretKey = secretKey;
	}

	/**
	 * 
	 * @param secretId your accessKey(secretId). you can get it by https://console.qcloud.com/capi
	 * @param secretKey your accessKey(secretId). you can get it by https://console.qcloud.com/capi
	 * @param bucketName 非空的该值 会覆盖config.json中的cosBucketName
	 * @param region     默认“ap-beijing”
	 */
	public static void setConfig(String secretId, String secretKey, String bucketName, String region) {
		CosClientUtil.secretId = secretId;
		CosClientUtil.secretKey = secretKey;
		if (StringUtils.isNotBlank(bucketName)) {
			CosClientUtil.bucketName = bucketName;
		}
		if (StringUtils.isNotBlank(region)) {
			CosClientUtil.region = region;
		}
	}
}
