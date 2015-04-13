package edu.xidian.mti1001.virty.activity3rd;

import java.util.ArrayList;

import edu.xidian.mti1001.virty.welcome.R;

//食物的食谱数组，在此进行一些初始化
public class FoodRecipeArray {
	// 数组
	private ArrayList<FoodRecipe> recipes;
	private FoodRecipe foodRecipe;

	// 构造函数
	public FoodRecipeArray() {
		// 对数组进行一些初始化
		initFoodRecipes();
	}

	// String []recipes = new String[]{"牛肉山药粥","桂圆粥","皮蛋瘦肉粥","营养咸豆花",
	// "蘑菇蔬菜意面","海蛎烧豆腐","蟹黄银杏汤"};
	private void initFoodRecipes() {
		recipes = new ArrayList<FoodRecipe>();
		// TODO Auto-generated method stub
		foodRecipe = new FoodRecipe("牛肉山药粥", R.drawable.recipe1,
				"牛肉50g，山药200g，西芹20g，生菜50g，大米100g，白芝麻少许，高汤。",
				"1. 牛肉切小片，山药去皮切小块，西芹切小丁，生菜洗净切丝备用。" + '\n'+ 
				"2. 山药和米、高汤一同煮粥，将熟烂时加进西芹、牛肉片拌熟，起锅前加入生菜，加适量盐调味，吃时可撒少许白芝麻.");
		recipes.add(foodRecipe);
		foodRecipe = new FoodRecipe("山药粥", R.drawable.recipe2,
				"桂圆25g，粳米100g，白糖少许。",
				"将桂圆同粳米共入锅中，加适量的水，熬煮成粥，调入白糖即成。");
		recipes.add(foodRecipe);
		foodRecipe = new FoodRecipe("皮蛋瘦肉粥", R.drawable.recipe3,
				"大米150g，猪肉200g（或牛里脊），皮蛋2个，盐和清水。",
				"1. 将大米淘洗干净，放入水，倒入香油搅匀后放置一旁，浸泡30分钟。姜去皮切成细思，香葱切碎。皮蛋切成小块。" + '\n'+ 
				"2. 把肉先切成片，再切丝，最后切成小颗粒。放入碗中，加入1/4茶匙（1克）盐，搅匀后腌制20分钟。"+'\n'+
				"3. 锅中倒入清水，大火煮开后，将肉里倒入煮一会儿，当水面有浮沫时，用勺子彻底撇干净，然后倒入一半的皮蛋块，再倒入姜丝，"+
					"煮约2分钟后，倒入浸泡后的米，改成小火煮40分钟，期间每隔5分钟用勺子沿同一方向搅拌一次，以免皮蛋粘锅底"+'\n'+
				"4. 最后，将剩下的一半皮蛋倒入，继续煮10分钟即可，喝前调入剩余的盐和香葱碎。");
		recipes.add(foodRecipe);
		foodRecipe = new FoodRecipe("营养咸豆花", R.drawable.recipe4,
				"超嫩豆腐一盒，裙带菜，蘑菇，胡萝卜，炒熟的松仁，香菜，葱姜蒜。",
				"1. 将裙带菜清洗干净泡发后切成小片，其他食材切成末和小丁。" + '\n'+ 
				"2. 将超嫩豆腐放在开水中煮1~2分钟。"+'\n'+
				"3. 等锅热后加入适量油，下入葱姜蒜末，爆香，之后再倒入其他配料翻炒半分钟，之后加入适量生抽、糖和陈醋进行调味。"+'\n'+
				"4. 豆腐煮好后切成小薄块，并盛到碗中，在上面淋上调料，还可以依照个人口味，加入炒好的松子、香菜碎和橄榄油，拌匀后即可食用。");
		recipes.add(foodRecipe);
		foodRecipe = new FoodRecipe("蘑菇蔬菜意面", R.drawable.recipe5,
				"三色螺旋面100克，蘑菇100克，火腿片50克，菠菜100克，蒜头3瓣，盐2克，白胡椒粉2克。",
				"1. 在锅中加入适量清水并烧开，之后加入5克盐和几滴橄榄油，之后下入三色螺旋面，并依照包装上注释的时间煮熟，" +
				"捞出沥干水分备用" + '\n'+ 
				"2. 将从头、洋葱和火腿切碎，蘑菇切成片，菠菜去掉根部切成段。"+'\n'+
				"3. 锅热后加适量橄榄油，等油温合适后下入切好的洋葱和蒜末炒香，之后再下入蘑菇，将蘑菇炒软。"+'\n'+
				"4. 加入淡奶油后再加入适量清水煮开。"+'\n'+
				"5. 把准备好的三色螺旋面和菠菜下入，并加入适量盐进行调味，翻炒1分钟后下入切好的火腿和白胡椒粉，搅拌均匀即可。");
		recipes.add(foodRecipe);
		foodRecipe = new FoodRecipe("海蛎烧豆腐", R.drawable.recipe6,
				"海蛎、豆腐、青蒜、地瓜粉、胡椒粉、盐、油、姜、醋、生抽、料酒。",
				"1. 将豆腐用淡盐水浸泡半个小时，再切成块，海蛎拌入适量盐、胡椒粉、地瓜粉、醋和料酒。青葱切成段，姜切成片备用。" +'\n'+ 
				"2. 锅中加入适量油，等油温上来后下入青葱段和姜片，以及豆腐，以小火略煎。"+'\n'+
				"3. 接着转为大火，倒入适量料酒和生抽进行翻炒。"+'\n'+
				"4. 再加入清水后炖煮片刻。"+'\n'+
				"5. 之后就可以下入海蛎焖煮一会，煮到汤汁变稠即可。"+'\n'+
				"6. 最后一步，下入蒜叶、盐、胡椒粉进行调味。");
		recipes.add(foodRecipe);
		foodRecipe = new FoodRecipe("蟹黄银杏汤", R.drawable.recipe7,
				"蟹黄200克，生银杏400克，生鸡肉150克，猪五花肉150克，熟猪油25克，胡椒粉1克，鲜汤500克。",
				"1. 将银杏敲碎，除去壳，放入沸水锅内，去皮抽心，洗净。猪肉切块。" +'\n'+ 
				"2. 炒锅放油烧至七成热，放入肉块、鸡块，煸炒至变色，加入精盐、胡椒粉、鲜汤。烧沸后，倒入砂锅，加银杏，" +
				"煨至银杏裂缝捞出。"+'\n'+
				"3. 用另一只砂锅，倒入鲜汤，加入蟹黄、胡椒粉、精盐烧开，倒入原砂锅中，调好口味盛入碗内即成。"+'\n'+
				"4. 此汤具有清热滋阴、敛肺止咳功效。对肺病、咳嗽、哮喘、气管炎等症，有一定的辅助疗效。");
		recipes.add(foodRecipe);
	}
	
	//根据位置返回相应的Food
	public FoodRecipe getItem(int position){
		return recipes.get(position);
	}
	
	public ArrayList<FoodRecipe> getRecipes(){
		return this.recipes;
	}
}
