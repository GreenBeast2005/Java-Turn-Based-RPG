public class WorldSizeInformation {
    public static final int GAME_WORLD_SIZE_X = 3000;
	public static final int GAME_WORLD_SIZE_Y = 2040;
    
    //Setting this up so that I only have to change the grid size in one place
	public static final int GRID_SIZE = 100;
	//This function makes sure that everytime a mouse positon is fed to a funcion, it adheres to the coordinate system.
	public static int CONVERT_CORD_TO_GRID(int t)
	{
		return t - (t % GRID_SIZE);
	}
}
