package prefabs;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
//import javafx.scene.Node;	//only needed if you want to give the button an image
import javafx.scene.control.Button;

public class ExportButton extends Button {

	//Only one constructor is used, as there's only ever 1 kinds of ExportButton
	//the functionality/style split is for readability only
	public ExportButton() {
		//functionality
		//sets onExportClickEvent.handle to be called every time the button is clicked or
		//	this.fire() is called
		this.setOnAction(new onExportClickEvent());
		//style
		this.setText("Export");
		this.setMinSize(200, 100);
		this.setMaxSize(1000, 500);
		this.setPrefSize(300, 150);
		this.relocate(400, 300);
	}

	//These constructors take button label and image as arguments, if desired
	/*public ExportButton(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ExportButton(String arg0, Node arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}*/
}

class onExportClickEvent implements EventHandler<ActionEvent> {
	//put the stuff you want the button to do in this method here
	@Override
	public void handle(ActionEvent actionEvent) {
		System.out.print("hello!");
	}
}