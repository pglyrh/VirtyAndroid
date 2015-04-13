package edu.xidian.mti1001.virty.activity3rd;

//健身方案，包括类型、方案名、频度、数量
public class HealthScheme{
	//成员
	private int category;		//类型，0代表健身房方案，1代表居家健身
	private int sex;		//性别，0代表男，1代表女
	private String schemeName;	//名
	private String frequency;		//频度
	private String quantity;	//数量
	
	//构造函数
	public HealthScheme(int category,int sex,String schemeName, String frequency,
			String quantity) {
		super();
		this.schemeName = schemeName;
		this.category = category;
		this.sex = sex;
		this.frequency = frequency;
		this.quantity = quantity;
	}
		
	//getter和setter
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
}
