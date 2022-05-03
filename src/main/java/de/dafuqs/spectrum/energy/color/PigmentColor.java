package de.dafuqs.spectrum.energy.color;

import net.minecraft.util.DyeColor;

import java.util.HashMap;

public class PigmentColor {
	
	public static ElementalColor CYAN = new ElementalColor(DyeColor.CYAN);
	public static ElementalColor MAGENTA = new ElementalColor(DyeColor.MAGENTA);
	public static ElementalColor YELLOW = new ElementalColor(DyeColor.YELLOW);
	public static ElementalColor BLACK = new ElementalColor(DyeColor.BLACK);
	public static ElementalColor WHITE = new ElementalColor(DyeColor.WHITE);
	
	public static CMYKColor ORANGE = new CompoundColor(DyeColor.ORANGE, new HashMap<>() {{ put(MAGENTA, 1F/3F); put(YELLOW, 2F/3F); }});
	public static CMYKColor LIME = new CompoundColor(DyeColor.LIME, new HashMap<>() {{ put(CYAN, 1F/3F); put(YELLOW, 2F/3F); }});
	public static CMYKColor PINK = new CompoundColor(DyeColor.PINK, new HashMap<>() {{ put(MAGENTA, 2F/3F); put(YELLOW, 1F/3F); }});
	public static CMYKColor RED = new CompoundColor(DyeColor.RED, new HashMap<>() {{ put(MAGENTA, 2F/4F); put(YELLOW, 2F/4F); }});
	public static CMYKColor LIGHT_BLUE = new CompoundColor(DyeColor.LIGHT_BLUE, new HashMap<>() {{ put(CYAN, 3F/4F); put(MAGENTA, 1F/4F); }});
	public static CMYKColor GREEN = new CompoundColor(DyeColor.GREEN, new HashMap<>() {{ put(CYAN, 2F/5F); put(MAGENTA, 1F/5F); put(YELLOW, 2F/5F); }});
	public static CMYKColor BLUE = new CompoundColor(DyeColor.BLUE, new HashMap<>() {{ put(CYAN, 3F/6F); put(MAGENTA, 2F/6F); put(YELLOW, 1F/6F); }});
	public static CMYKColor PURPLE = new CompoundColor(DyeColor.PURPLE, new HashMap<>() {{ put(CYAN, 2F/6F); put(MAGENTA, 3F/6F); put(YELLOW, 1F/6F); }});
	public static CMYKColor BROWN = new CompoundColor(DyeColor.BROWN, new HashMap<>() {{ put(MAGENTA, 2F/5F); put(YELLOW, 1F/5F); put(BLACK, 2F/5F); }});
	
	public static CMYKColor LIGHT_GRAY = new CompoundColor(DyeColor.LIGHT_GRAY, new HashMap<>() {{ put(CYAN, 2F/6F); put(MAGENTA, 2F/6F); put(YELLOW, 2F/6F); }});
	public static CMYKColor GRAY = new CompoundColor(DyeColor.GRAY, new HashMap<>() {{ put(CYAN, 2F/6F); put(MAGENTA, 2F/6F); put(YELLOW, 2F/6F); }});
	
}