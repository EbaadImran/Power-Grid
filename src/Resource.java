
public enum Resource {
	COAL, OIL, GARBAGE, URANIUM, DOUBLE, NONE;

	public static int resourceToNum(Resource res) {
		for (int i = 0; i < Resource.values().length; i++)
			if (res == Resource.values()[i])
				return i;
		return -1;
	}

	public static Resource numToResource(int num) {
		return Resource.values()[num];
	}
}
