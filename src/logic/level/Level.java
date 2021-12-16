package logic.level;
/**
 * A public class that contain all levels.
 * @author Mos
 *
 */
public class Level {
	
	// X = finish point
	// M = coin
	// K = book1 (exp)
	// k = book2 (exp)
	// H = hurt block (1)
	// H = hurt block (2)
	
	private static final String[] LEVEL1 = new String[] {
			"000000000000000000000000000000000k00k00000000000000000MM000000000000k0000000M00000000k00000000000000",
			"00000000000000000M50000000000000K0000000MM00000000000000000000D00D0000000000000D00D0000k000000000000",
			"000000ABBBC00000012K0000000000050000000ABC000000K01200000ABBC0000000000000000D000000000000000000MMM0",
			"000012KKMM000000122300H000000012223MMMM000000000122230005000000000MMM0000050M0000Kk00000000000000X00",
			"2222HH222222222222222223001222222222222HHH2222222222222222222222222222222222222222222222222222222222"

	};
	
	private static final String[] LEVEL2 = new String[] {
			"00000M00MM0000000000000000MM0000000000000000KK0000000000000000000000000MMM00000000000000000000000000",
			"0000000000000K00000000500000000D00D000D000D000000000000000D00000000KKM000000000000000000000000000000",
			"00000000ABC00000000001230000ABC00000000MM000000000kkMM00D000000K00123000000000000000000ABBBBC00000M0",
			"0000001200K0000000001222300000005000000000005000KKMM0005000000000122200000000000000000500X00000000KK",
			"22222222HHH22300122222222222222222222222HHHH22222222222222H22H22222HH2222222222222222222222222222222"

	};
	
	private static final String[] LEVEL3 = new String[] {
			"0000000000000000000000000000000000000D0050000000000K000000000000000000000000000000000000000ACMAC0000",
			"00000000000000000000000000000000000D0MM020000000000000D000000000000000000000000000000000000005000000",
			"000000K000000000K0000000D00D00DKKD000D0020D000D000000000D000000000000000000000MM0000000000ABBBBBCk00",
			"0000000000500D000000005MMMM00000005000002KK00000000MMMMMM05000KM00kM00kM00KM00MM00000000500000003k0X",
			"22222H22H2230000123012222230012HH222H0H2222301230012222222222200220022002200220022222222222H0HH22222",

	};
	
	private static final String[] LEVEL4 = new String[] {
			"000K0K0K0000MM000000000000000MM0000K000000000000000000000000000000000000",
			"000005000000KK000000000000000M000MM000000000M00000000000000000000000D000",
			"0000M200000D00D00000000D0D0D0000013000000M0KkK0M00000D000ABBBBC0000D0000",
			"000M02000500000050000050K000K00012230000M5000005M00000000MMMMMM000500X0K",
			"222222HH2230000122230122230122222HH2222222HH0HH2222HH0HH2HHHHHH222222220",

	};
	
	private static final String[] LEVEL5 = new String[] {
			"0000000000000000000000000K000000000000MM00000000000000000000",
			"00K00MM000M0000000D0000D0000ABC00000000000000000000000000000",
			"0000D000D000000000000D0000000000kk0D0000000000M0000000000000",
			"000000D0000AC000K0ABC00000D000KK0D000MMM0000K0D00000K0X00000",
			"0ABBBC00000000D00D000D00AC00D00D000000D000ABBC000ABBBC000000"
	};
	
	public static final String[][] ALL_LEVEL = new String[][] {
		LEVEL1, LEVEL2, LEVEL3, LEVEL4, LEVEL5
	};
}