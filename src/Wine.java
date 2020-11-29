
public class Wine extends Product{

	int year;

	public Wine(String name, int price) {
		super(name, price, true);
	}

	public Integer getYear() {
		return year;
	}


}
