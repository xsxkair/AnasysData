package com.xsx.ncd.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXTextArea;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.xsx.ncd.entity.Card;
import com.xsx.ncd.entity.TestData;
import com.xsx.ncd.repository.CardRepository;
import com.xsx.ncd.repository.TestDataRepository;
import com.xsx.ncd.spring.SpringFacktory;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class AnasysData extends Application{
	
	private AnchorPane rootPane;
	private Scene scene = null;
	
	@FXML LineChart<Number, Number> myChart;
	@FXML NumberAxis YNumberAxis;
	@FXML Label IDLabel;
	
	@FXML Label DeviceTLabel;
	@FXML Label DeviceBLabel;
	@FXML Label DeviceCLabel;
	
	@FXML ImageView yes;
	@FXML ImageView error;
	@FXML Label Label1;
	@FXML Label Label2;
	@FXML Label Label3;
	@FXML Label Label4;
	@FXML Label Label5;
	@FXML Label Label6;
	
	private TestDataRepository testDataRepository;
	private CardRepository cardRepository;
	private ObjectMapper mapper = new ObjectMapper();
	private JavaType javaType = null;
	private List<Double> seriesdata = new ArrayList<>();
	private Series<Number, Number> series = new Series<>();
	private int size = 0;

	private final String fileName = "e:\\image\\data.xls";

	public void UI_Init(){
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/test/AnasysDataPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/test/AnasysDataPage.fxml");
        loader.setController(this);
        try {
        	rootPane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        myChart.getData().addAll(series);
        myChart.setAnimated(false);
        
        error.visibleProperty().bind(yes.visibleProperty().not());

        loader = null;
        in = null;
	}

	@Override
	public void start(Stage arg0) {
		// TODO Auto-generated method stub
		SpringFacktory.SpringFacktoryInit();
		
		this.UI_Init();
		scene = new Scene(rootPane);
		arg0.setScene(scene);
		arg0.show();
		arg0.setOnCloseRequest(e->{
			System.exit(0);
		});
		
		this.anasysDataFun();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	private void anasysDataFun(){
		int i=0, j=0, k=0;
		List<Double> tempd = null;
		List<Double> tempd2 = new ArrayList<>();
		Integer t, b, c;
		StackPane stackPane;
		StackPane stackPane1;
		double tempv1 = 0;
		MyPoint tempTPoint = new MyPoint();
		MyPoint tempBPoint = new MyPoint();
		MyPoint tempCPoint = new MyPoint();
		MyPoint tempPoint = new MyPoint();

		TestData testData = null;
		
		Card card = null;
		int t_l = 0;
		int c_l = 0;
		double tempcv1 = 0;
		double tempcv2 = 0;
		double tempcv3 = 0;
		double tempcv4 = 0;

		testDataRepository = SpringFacktory.getCtx().getBean(TestDataRepository.class);
		cardRepository = SpringFacktory.getCtx().getBean(CardRepository.class);
		javaType = mapper.getTypeFactory().constructParametricType(List.class, Double.class);

		for(j=2600; j<2611; j++) {
			testData = testDataRepository.findOne(j);

	        if(testData != null){
	
	        	card = cardRepository.findCardByCid(testData.getCid());
	        	if(card == null)
	        	{
	        		continue;
	        	}

	        	Label1.setText(null);
	        	Label2.setText(null);
	        	Label3.setText(null);
	        	Label4.setText(null);
	        	Label5.setText(null);
	        	
	        	seriesdata.clear();
	        	series.getData().clear();
	        	IDLabel.setText(String.format("%s-%s", testData.getCid(), testData.getCnum()));

		        try {
		        	if(testData.getSerie_a() != null){
		        		tempd = mapper.readValue(testData.getSerie_a(), javaType);
	    		        seriesdata.addAll(tempd);
		        	}
			        
		        	if(testData.getSerie_b() != null){
		        		tempd = mapper.readValue(testData.getSerie_b(), javaType);
	    		        seriesdata.addAll(tempd);
		        	}
			       
		        	if(testData.getSerie_c() != null){
		        		tempd = mapper.readValue(testData.getSerie_c(), javaType);
	    		        seriesdata.addAll(tempd);
		        	}

				} catch (Exception e) {
					continue;
				}
		        
		        //显示原始曲线
		        t = testData.getT_l();
		        b = testData.getB_l();
		        c = testData.getC_l();
		        size = seriesdata.size();
		        if(size != 300)
		        	continue;
		        
		        tempBPoint.resetMyPoint();
		        tempCPoint.resetMyPoint();
		        tempTPoint.resetMyPoint();
		        tempPoint.resetMyPoint();
		        for(i=0; i<size; i++)
		        {
		        	tempv1 = seriesdata.get(i);
		        	Data<Number, Number> data = new Data<Number, Number>(i, tempv1);
		        	series.getData().add(data);
		        	stackPane = (StackPane) data.getNode();
		        	stackPane.setPrefSize(1, 1);
				}
		        
		        if(t != null) {
		        	stackPane = (StackPane) series.getData().get(t).getNode();
		        	stackPane.setStyle("-fx-background-color:red");
		        	stackPane.setPrefSize(10, 10);
	        		
		        	DeviceTLabel.setText(String.format("(%d, %.0f)", t, seriesdata.get(t)));
	        	}

		        if(c != null){
		        	stackPane = (StackPane) series.getData().get(c).getNode();
			        stackPane.setStyle("-fx-background-color:red");
		        	stackPane.setPrefSize(10, 10);
		        	DeviceCLabel.setText(String.format("(%d, %.0f)", c, seriesdata.get(c)));
	        	}
		        
		        t_l = card.getT_l();
		        c_l = card.getC_l();
		        //找T
		        findFeng(seriesdata, t_l-20, t_l, t_l+20, tempTPoint);
		        if(tempTPoint.x == 0)
		        {
		        	yes.setVisible(false);
		        	Label1.setText("找不到T线");
		        	saveImage(testData);
		        	continue;
		        }
		        else
		        {
		        	stackPane = (StackPane) series.getData().get(tempTPoint.x).getNode();
				    stackPane.setStyle("-fx-background-color:blue");
				    stackPane.setPrefSize(10, 10);
				    
		        	Label1.setText(String.format("(%d, %.0f)", tempTPoint.x, tempTPoint.y));
		        }
		        
		        //残留检测
		        tempd = seriesdata.subList(20, tempTPoint.x-20);
		        for(i=20; i<tempTPoint.x-20; i++)
	        	{
	        		stackPane = (StackPane) series.getData().get(i).getNode();
				    stackPane.setStyle("-fx-background-color:black");
				    stackPane.setPrefSize(5, 5);
	        	}
		        tempcv1 = getDataCV(tempd);
		        if(tempcv1 > 0.05)
		        {
		        	yes.setVisible(false);
		        	Label2.setText(String.format("cv：%.4f > 0.05, 有残留", tempcv1));
		        	
		        	saveImage(testData);
		        	continue;
		        }
		        else
		        {
		        	Label2.setText(String.format("cv：%.4f < 0.05, 无残留", tempcv1));
		        }
		       
		        //找C
		        findFeng(seriesdata, c_l-20, c_l, c_l+20, tempCPoint);
		        if(tempCPoint.x == 0)
		        {
		        	yes.setVisible(false);
		        	Label3.setText("找不到C线");
		        	saveImage(testData);
		        	continue;
		        }
		        else
		        {
		        	stackPane = (StackPane) series.getData().get(tempCPoint.x).getNode();
				    stackPane.setStyle("-fx-background-color:blue");
				    stackPane.setPrefSize(10, 10);
				    
		        	Label3.setText(String.format("(%d, %.0f)", tempCPoint.x, tempCPoint.y));
		        }

/*		        //T-C距离
		        i = tempCPoint.x - tempTPoint.x;
		        logTextArea.appendText(String.format("%d\r\n", i));
		        if((i < 70) && (i > 90))
		        {
		        	ResultLabel7.setText(String.format("%d, T-C距离错误", i));
		        	saveImage(testData);
		        	continue;
		        }
		        */
		        //找基线
		        tempBPoint.y = 20000;
		        tempBPoint.x = 0;
		        for(i=tempTPoint.x; i<tempCPoint.x; i++)
		        {
		        	if(tempBPoint.y > seriesdata.get(i))
		        	{
		        		tempBPoint.x = i;
		        		tempBPoint.y = seriesdata.get(i);
		        	}
		        }
		        stackPane = (StackPane) series.getData().get(tempBPoint.x).getNode();
				stackPane.setStyle("-fx-background-color:blue");
	        	stackPane.setPrefSize(10, 10);
		        Label4.setText(String.format("(%d, %.0f)", tempBPoint.x, tempBPoint.y));
		        
		        //基线分析
		        for(i=tempTPoint.x+25; i<tempCPoint.x-25; i++)
	        	{
		        	if(i != tempBPoint.x)
		        	{
			        	stackPane = (StackPane) series.getData().get(i).getNode();
					    stackPane.setStyle("-fx-background-color:black");
					    stackPane.setPrefSize(5, 5);
		        	}
	        	}
		        tempd = seriesdata.subList(tempTPoint.x+25, tempCPoint.x-25);
		        tempcv1 = getDataCV(tempd);
		        if(tempcv1 > 0.05)
		        {
		        	yes.setVisible(false);
		        	Label5.setText(String.format("cv：%.4f > 0.05, 错误", tempcv1));
		        	
		        	saveImage(testData);
		        	continue;
		        }
		        else
		        {
		        	Label5.setText(String.format("cv：%.4f < 0.05, 正常", tempcv1));
		        }
		        
		        //末尾分析
		        for(i=tempCPoint.x+25; i<300; i++)
	        	{
	        		stackPane = (StackPane) series.getData().get(i).getNode();
				    stackPane.setStyle("-fx-background-color:black");
				    stackPane.setPrefSize(5, 5);
	        	}
		        tempd = seriesdata.subList(tempCPoint.x+25, 300);
		        tempcv1 = getDataCV(tempd);
		        if(tempcv1 > 0.05)
		        {
		        	yes.setVisible(false);
		        	Label6.setText(String.format("cv：%.4f > 0.05, 错误", tempcv1));
		        	
		        	saveImage(testData);
		        	continue;
		        }
		        else
		        {
		        	Label6.setText(String.format("cv：%.4f < 0.05, 正常", tempcv1));
		        }
		        
		        yes.setVisible(true);
		        saveImage(testData);
			}
		}
       
		System.exit(0);
	}
	
	private void saveImage(TestData testData) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        Image image = rootPane.snapshot(null, null);
	    try {
	        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png",
	                new File("e:\\image\\" + testData.getCid()+testData.getCnum() + ".png"));
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private void findFeng(List<Double> datas, int startIndex, int midIndex, int endIndex, MyPoint myPoint)
	{
		int i=0, j=0;
		double juli = 10000;
		double tempv1 = 0;
		MyPoint tempPoint = new MyPoint();
		
		myPoint.resetMyPoint();
        for(i=startIndex; i<endIndex; i++)
        {
        	//找最大值
        	tempPoint.resetMyPoint();
        	for(j=i-15; j<15+i; j++) 
        	{
        		tempv1 = datas.get(j);
        		if(tempPoint.y < tempv1) 
        		{
        			tempPoint.y = tempv1;
        			tempPoint.x = j;
        		}
        	}

        	//阶段2： 判断最大值位置是不是封顶
        	for(j=0; j<10; j++) 
        	{
        		//峰前必须递增
        		if(datas.get(tempPoint.x-j) < datas.get(tempPoint.x-j-1))
        			break;
        			
        		//峰后必须递减
        		if(datas.get(tempPoint.x+j) < datas.get(tempPoint.x+j+1))
        			break;
        	}
        	// 如果阶段结果失败，则找峰失败
        	if(j < 10)
        	{
        		i += 10;
        		continue;
        	}
        	else 
        	{
        		if(juli > Math.abs(tempPoint.x - midIndex))
        		{
        			juli = Math.abs(tempPoint.x - midIndex);

		        	myPoint.x = tempPoint.x;
		        	myPoint.y = tempPoint.y;
        		}
        		
        		i = (tempPoint.x + 15);
        	}
        }
	}
	
	private double aveag(List<Double> datas) {
		double sum = 0.0;
		
		for (double a : datas) {
			sum += a;
		}
		sum /= datas.size();
		return sum;
	}	
	
	private double getDataCV(List<Double> datas) {
		int size = datas.size();
		double sum = 0.0f;
		double tempv1 = 0;
		double tempv2 = 0;
		
		//找平均值
		sum = aveag(datas);
		
		//计算标准差
		for (double j : datas) {
			tempv1 = j;
			tempv1 -= sum;
			tempv1 *= tempv1;
			tempv2 += tempv1;
		}
		tempv2 /= size;
		tempv2 = Math.sqrt(tempv2);

		return tempv2 / sum;
	}
	
	class MyPoint{
		
		public int x;
		public double y;
		
		public MyPoint() {
			this.x = 0;
			this.y = 0;
		}
		
		public MyPoint(int x, double y) {
			this.x = x;
			this.y = y;
		}
		
		public void resetMyPoint() {
			this.x = 0;
			this.y = 0;
		}
	}
	
	private void saveData() {

		WritableWorkbook wb = null;
		FileOutputStream fileOut = null;
		jxl.write.Number number = null;
		WritableSheet sheet = null;
		int rownum = 0;

		
		try {
			fileOut = new FileOutputStream(fileName);
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			fileOut = null;
		}
		
		try {
			wb = Workbook.createWorkbook(fileOut);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			sheet = wb.getSheet(0);
		} catch (Exception e) {
			// TODO: handle exception
			sheet = null;
		}
		
		if(sheet == null)
			sheet = wb.createSheet("数据", 0);

		rownum = sheet.getRows();

/*
		        try {
		        	
		        	rownum++;
				    jxl.write.Label pihaoLabel = new jxl.write.Label(0, rownum, String.format("%s-%s", testData.getCid(), testData.getCnum())); 
			    	sheet.addCell(pihaoLabel);
			    
					//number = new jxl.write.Number(1, rownum, seriesdata.get(t)); 
					//sheet.addCell(number);
					number = new jxl.write.Number(2, rownum, cvDatasOfTC[0]); 
					sheet.addCell(number);
					number = new jxl.write.Number(3, rownum, cvDatasOfTC[1]); 
					sheet.addCell(number);
					number = new jxl.write.Number(4, rownum, Math.abs(cvDatasOfTC[0] - cvDatasOfTC[1])); 
					sheet.addCell(number);
					
					//number = new jxl.write.Number(6, rownum, seriesdata.get(c)); 
					//sheet.addCell(number);
					number = new jxl.write.Number(7, rownum, cvDatasOfTC[2]); 
					sheet.addCell(number);
					number = new jxl.write.Number(8, rownum, cvDatasOfTC[3]); 
					sheet.addCell(number);
					number = new jxl.write.Number(9, rownum, Math.abs(cvDatasOfTC[2] - cvDatasOfTC[3])); 
					sheet.addCell(number);
		    	 
		        } catch (RowsExceededException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (WriteException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
*/
		
		try {
			wb.write();
			
			 // 关闭文件  
		   	 wb.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
