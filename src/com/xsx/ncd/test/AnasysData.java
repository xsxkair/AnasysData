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
	@FXML Label IDLabel1;
	
	@FXML Label DeviceTLabel;
	@FXML Label DeviceLabel;
	@FXML Label DeviceCLabel;
	
	@FXML ImageView yes;
	@FXML ImageView error;
	@FXML Label Label1;
	@FXML Label Label2;
	@FXML Label Label3;
	@FXML Label Label4;
	@FXML Label Label5;
	@FXML Label Label6;
	@FXML Label Label7;
	@FXML Label Label8;
	
	private TestDataRepository testDataRepository;
	private CardRepository cardRepository;
	private ObjectMapper mapper = new ObjectMapper();
	private JavaType javaType = null;
	private List<Double> seriesdata = new ArrayList<>();
	private Series<Number, Number> series = new Series<>();
	private int size = 0;

	private final String fileName = "e:\\image\\data.xls";
	private StringBuffer imagePathStringBuffer = new StringBuffer();
	private int errorId[]= {
			726, 1533, 1722, 1715, 1714, 1539, 1502, 1619, 1588, 1512, 1613, 772, 1775, 2234, 2236, 1788, 2102, 2109, 2186, 2274, 2300, 2481,
			2375, 2317, 2434, 2456, 2485, 2296, 2261, 2431, 2328, 2426, 2636, 2641, 472, 298, 723, 1447, 726, 1296, 1358, 1677, 1234, 2440, 
			2437, 1019, 381, 380, 426, 385, 470, 412, 390, 428, 468, 395, 666, 	603, 640, 690, 593, 648, 677, 605, 576, 637, 667, 586, 658, 663,
			653, 628, 606, 649, 627, 617, 636, 632, 655, 671, 685, 686, 684, 630, 678, 639, 641, 651, 664, 620, 642, 1018, 1066, 1688, 1061, 
			1058, 1146, 1065, 1113, 1698, 1112, 1697, 1140, 2430, 2435, 2433, 1010, 924, 1365, 915, 988, 966, 1577, 1346, 1501, 1604, 1570, 
			1509, 1716, 1548, 1709, 1973, 1814, 2313, 2085, 2311, 2623, 2588, 2590, 2557, 2397, 2385, 2553, 2488, 2276, 2351, 2479, 2640, 2386, 2294,
			
	};

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

		for(j=800; j<2660; j++) {
		//for(j=0; j< errorId.length; j++) {
			testData = testDataRepository.findOne(j);
			IDLabel1.setText(String.format("%d", j));
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
	        	Label6.setText(null);
	        	Label7.setText(null);
	        	Label8.setText(null);
	        	
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
	        		
		        	DeviceTLabel.setText(String.format("(%d, %.0f)--%d", t, seriesdata.get(t), card.getT_l()));
	        	}

		        if(c != null){
		        	stackPane = (StackPane) series.getData().get(c).getNode();
			        stackPane.setStyle("-fx-background-color:red");
		        	stackPane.setPrefSize(10, 10);
		        	DeviceCLabel.setText(String.format("(%d, %.0f)--%d", c, seriesdata.get(c), card.getC_l()));
	        	}
		        
		        if(testData.getT_re() != null)
		        	DeviceLabel.setText(testData.getT_re());
		        else
		        	DeviceLabel.setText("无");
		        
		        tempcv1 = getDataCV(seriesdata, 0);
		        if(tempcv1 < 0.01)
		        {
		        	yes.setVisible(false);
		        	Label6.setText(String.format("cv %.4f < 0.01, 未加样", tempcv1));
		        	Label6.setStyle("-fx-text-fill:red");
		        	saveImage(testData, false);
		        	continue;
		        }
		        else
		        {
		        	Label6.setText(String.format("cv %.4f > 0.01", tempcv1));
		        	Label6.setStyle("-fx-text-fill:black");
		        }
		        
		        t_l = card.getT_l();
		        c_l = card.getC_l();
		        //找T
		        findFeng(seriesdata, t_l-30, t_l, t_l+30, tempTPoint);
		        if(tempTPoint.x == 0)
		        {
		        	//如果没找到峰，则判断是不是峰是平的，如果是，则在t线点取一点为峰
		            tempd = seriesdata.subList(t_l-15, t_l+15);
		            tempv1 = getDataCV(tempd, 0);
		            if(tempv1 < 0.05)
		            {
		            	tempTPoint.x = t_l;
		            	tempTPoint.y = seriesdata.get(t_l);
		            	Label1.setText(String.format("(%d, %.0f) -- %.4f", tempTPoint.x, tempTPoint.y, tempv1));
		            }
		            else
		            {
		            	yes.setVisible(false);
			        	Label1.setText(String.format("%.4f > 0.05, 找不到T线", tempv1));
			        	Label1.setStyle("-fx-text-fill:red");
			        	saveImage(testData, false);
			        	continue;
		            }
		        }
		        else
		        {
		        	Label1.setText(String.format("(%d, %.0f)", tempTPoint.x, tempTPoint.y));
				}

		        stackPane = (StackPane) series.getData().get(tempTPoint.x).getNode();
		        stackPane.setStyle("-fx-background-color:blue");
		        stackPane.setPrefSize(10, 10);

		        Label1.setStyle("-fx-text-fill:black");
		        
		        //残留检测
		        
		        tempv1 = 0;
		        tempcv2 = 0;
		        for(i=20; i<tempTPoint.x-30; i++)
	        	{
		        	tempv1 += seriesdata.get(i);
				    if(i >= 35)
				    {
				    	tempd = seriesdata.subList(20, i+1);
			        	tempcv1 = getDataCV(tempd, tempv1);
			        	if(tempcv2 < tempcv1)
			        	{
			        		tempcv2 = tempcv1;
			        		k = i;
			        	}
				    }
	        	}
		        for(i=20; i<k; i++)
	        	{
	        		stackPane = (StackPane) series.getData().get(i).getNode();
				    stackPane.setStyle("-fx-background-color:black");
				    stackPane.setPrefSize(5, 5);
	        	}

		        if(tempcv2 > 0.15)
		        {
		        	yes.setVisible(false);
		        	Label2.setText(String.format("cv：%.4f > 0.15, 有残留", tempcv2));
		        	Label2.setStyle("-fx-text-fill:red");
		        	saveImage(testData, false);
		        	continue;
		        }
		        else
		        {
		        	Label2.setText(String.format("cv：%.4f < 0.15, 无残留", tempcv2));
		        	Label2.setStyle("-fx-text-fill: black");
		        }
		       
		        //找C
		        findFeng(seriesdata, c_l-30, c_l, c_l+30, tempCPoint);
		        if(tempCPoint.x == 0)
		        {
		        	//如果没找到峰，则判断是不是峰是平的，如果是，则在t线点取一点为峰
		            tempd = seriesdata.subList(c_l-15, c_l+15);
		            tempv1 = getDataCV(tempd, 0);
		            if(tempv1 < 0.05)
		            {
		            	tempCPoint.x = c_l;
		            	tempCPoint.y = seriesdata.get(c_l);
		            	Label3.setText(String.format("(%d, %.0f) -- %.4f", tempCPoint.x, tempCPoint.y, tempv1));
		            }
		            else
		            {
		            	yes.setVisible(false);
		            	Label3.setText(String.format("%.4f, 找不到C线", tempv1));
		            	Label3.setStyle("-fx-text-fill:red");
			        	saveImage(testData, false);
			        	continue;
		            }
		        }
		        else
		        {
		        	Label3.setText(String.format("(%d, %.0f)", tempCPoint.x, tempCPoint.y));
		        }
		        stackPane = (StackPane) series.getData().get(tempCPoint.x).getNode();
		        stackPane.setStyle("-fx-background-color:blue");
		        stackPane.setPrefSize(10, 10);

		        Label3.setStyle("-fx-text-fill:black");

		        //T-C距离
		        i = tempCPoint.x - tempTPoint.x;

		        if((i < 50) || (i > 100))
		        {
		        	yes.setVisible(false);
		        	Label7.setText(String.format("%d, T-C距离错误", i));
		        	Label7.setStyle("-fx-text-fill:red");
		        	saveImage(testData, false);
		        	continue;
		        }
		        else
		        {
		        	Label7.setText(String.format("%d, T-C距离正常", i));
		        	Label7.setStyle("-fx-text-fill : black");
		        }
		        
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
		        for(i=tempTPoint.x+30; i<tempCPoint.x-25; i++)
	        	{
		        	if(i != tempBPoint.x)
		        	{
			        	stackPane = (StackPane) series.getData().get(i).getNode();
					    stackPane.setStyle("-fx-background-color:black");
					    stackPane.setPrefSize(5, 5);
		        	}
	        	}
		        tempd = seriesdata.subList(tempTPoint.x+28, tempCPoint.x-22
		        		);
		        tempcv1 = getDataCV(tempd, 0);
		        if(tempcv1 > 0.1)
		        {
		        	yes.setVisible(false);
		        	Label5.setText(String.format("cv：%.4f > 0.1, 错误", tempcv1));
		        	Label5.setStyle("-fx-text-fill : red");
		        	saveImage(testData, false);
		        	continue;
		        }
		        else
		        {
		        	Label5.setText(String.format("cv：%.4f < 0.1, 正常", tempcv1));
		        	Label5.setStyle("-fx-text-fill : black");
		        }
		        
		        //T和C必须有一个搞峰
		        tempd = seriesdata.subList(tempTPoint.x, tempTPoint.x+20);
		        tempcv1 = getDataCV(tempd, 0);
		        
		        tempd = seriesdata.subList(tempCPoint.x, tempCPoint.x+20);
		        tempcv2 = getDataCV(tempd, 0);

		        if(tempcv1 < 0.1 && tempcv2 < 0.1)
		        {
		        	yes.setVisible(false);
		        	Label8.setText(String.format("cv和：(%.4f, %.4f) < 0.1, 俩峰都矮，错误", tempcv1, tempcv2));
		        	Label8.setStyle("-fx-text-fill : red");
		        	saveImage(testData, false);
		        	continue;
		        }
		        else 
		        {
		        	Label8.setText(String.format("cv和：(%.4f, %.4f) > 0.1, 正常", tempcv1, tempcv2));
		        	Label8.setStyle("-fx-text-fill : black");
				}
		        
		        yes.setVisible(true);
		        saveImage(testData, true);
			}
		}
       
		System.exit(0);
	}
	
	private void saveImage(TestData testData, boolean isRight) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        Image image = rootPane.snapshot(null, null);
	    try {
	    	imagePathStringBuffer.setLength(0);
	    	if(isRight)
	    		imagePathStringBuffer.append("e:\\image\\OK\\");
	    	else
	    		imagePathStringBuffer.append("e:\\image\\Error\\");
	    	
	    	imagePathStringBuffer.append(testData.getCid());
	    	imagePathStringBuffer.append(testData.getCnum());
	    	imagePathStringBuffer.append(".png");
	    	
	        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png",
	                new File(imagePathStringBuffer.toString()));
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private void findFeng(List<Double> datas, int startIndex, int midIndex, int endIndex, MyPoint myPoint)
	{
		int i=0, j=0;
		double juli = 10000;
		double tempv1 = 0;
		List<Double> tempd = null;
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
        		if(tempPoint.x+j+1 < 300)
        		{
        			if(datas.get(tempPoint.x+j) < datas.get(tempPoint.x+j+1))
        				break;
        		}
        			
        	}
        	// 如果阶段结果失败，则找峰失败
        	if(j < 10)
        	{
        		i += 10;
        		continue;
        	}
        	else 
        	{
        		if(tempPoint.y > myPoint.y)
        		{
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
	
	private double getDataCV(List<Double> datas, double ssum) {
		int size = datas.size();
		double sum = 0.0f;
		double tempv1 = 0;
		double tempv2 = 0;
		
		//找平均值
		if(ssum == 0)
			sum = aveag(datas);
		else
			sum = ssum/size;
		
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
