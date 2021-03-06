package domain.valueo;

public class Mailaddress extends Value{

	private String mailAddress;		// メールアドレス

	/* コンストラクタ */
	public Mailaddress(String mailAddress) throws InvalidMailAddressFormat{
		super();
		if(mailAddress.indexOf("@") == -1){
			String message = "メールアドレスの形式が正しくありません。";
			throw new InvalidMailAddressFormat(message);
		}

		this.mailAddress = mailAddress;
	}

	// メールアドレスを取得
	public String getMailAddress(){
		return mailAddress;
	}

	@Override
	public String toString() {
		return mailAddress;
	}


}
