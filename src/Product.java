import java.util.Objects;

public abstract class Product {
	private final String name;
	private final int price;
	private boolean ageVerify;
	
	public Product(String name, int price, boolean f) {
		this.name = name;
		this.price = price;
		this.ageVerify = f;
	}

	public boolean needAgeVerify(){
		return this.ageVerify;
	}
	
	public String getName(){
		return this.name;
	}

	public int getPrice(){
		return this.price;
	}

	public String toString(){
		return this.getName() + " " + this.getPrice();
	}

	public static Product productFactory(String name, int price){
		if (name.contains("wine")){
			return new Wine(name, price);
		} if (name.contains("sake")){
			return new Sake(name, price);
		} else {
			return new OtherProduct(name, price);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Product product = (Product) o;
		return price == product.price &&
				Objects.equals(name, product.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, price);
	}
}
