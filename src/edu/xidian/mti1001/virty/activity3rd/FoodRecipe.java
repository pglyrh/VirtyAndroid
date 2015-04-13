package edu.xidian.mti1001.virty.activity3rd;

import java.io.Serializable;

//食物的食谱，包括名字、图片、成分、做法
//为了能让Intent传递对象，类必须实现Serializable接口
public class FoodRecipe implements Serializable{
	//成员
	private String foodName;	//名
	private int imageId;		//图片ID
	private String material;	//原材料
	private String method;		//制作方法
	
	//构造函数
	public FoodRecipe(String foodName, int imageId, String material,
			String method) {
		super();
		this.foodName = foodName;
		this.imageId = imageId;
		this.material = material;
		this.method = method;
	}
		
	//getter和setter
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
	@Override
	//只返回名称
	public String toString() {
		return getFoodName();
	}
	
}
