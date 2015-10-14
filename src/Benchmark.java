import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class Benchmark
{
	private HashMap<Long, Integer> framesInSeconds = new HashMap<>();

	private int getCurrentFrameCount()
	{
		return framesInSeconds.get(getTimestamp());
	}

	private int getFrameCount()
	{
		return framesInSeconds.get(getTimestamp() - 1);
	}

	public void addFrame()
	{
		int frames = 0;

		try
		{
			frames = getCurrentFrameCount();
		}
		catch (NullPointerException e) {}

		framesInSeconds.put(getTimestamp(), frames + 1);
	}

	private long getTimestamp()
	{
		return Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() / 1000L;
	}

	public String getText()
	{
		int frameCount = 0;

		try
		{
			frameCount = getFrameCount();
		}
		catch (NullPointerException e) {}

		return String.format("Frames per second: %d", frameCount);
	}
}