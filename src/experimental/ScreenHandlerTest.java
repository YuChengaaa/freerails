package experimental;

import javax.swing.JFrame;
import jfreerails.client.GameLoop;
import jfreerails.client.ScreenHandler;

public class ScreenHandlerTest extends JFrame {

	public static void main(String[] args) {
		JFrame frame = new ScreenHandlerTest();
		frame.show();
		ScreenHandler sh = new ScreenHandler(frame, false);
		GameLoop gl=new GameLoop(sh);
		gl.run();
	}

}