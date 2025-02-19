
package it.polito.tdp.borders;

import java.net.URL;
import java.util.ResourceBundle;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    private ComboBox<String> cmbBox;
    @FXML
    private Button btnStatiRaggiungibili;
    
    
    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	txtResult.clear();
    	Integer anno=Integer.parseInt(txtAnno.getText());
    	if(anno<1816 || anno>2016) {
			txtResult.setText("Inserire un anno compreso tra 1816 e 2016!");
			return;
		}
    	Graph<Country,DefaultEdge> grafo=model.creaGrafo(anno);
    	txtResult.appendText("Numero componenti connesse: "+model.getNumberOfConnectedComponents()+"\n");
    	for(Country c:grafo.vertexSet())
    		txtResult.appendText(c.getNome()+" "+model.getNConfini(c)+"\n");
       }
    @FXML
    
    void doStatiRaggiungibili(ActionEvent event) {
    	txtResult.clear();
    	for(Country c:model.confiniRaggiungibili(model.trovaPaese((String) cmbBox.getValue())))
    		txtResult.appendText(c.getNome());
    	
    }
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
         }
    
    public void setModel(Model model) {
    	this.model = model;
    	for(Country c:model.getGrafo().vertexSet())
   		 cmbBox.getItems().add(c.getNome()); 
    }
}
