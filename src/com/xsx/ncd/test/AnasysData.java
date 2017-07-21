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
import com.xsx.ncd.entity.TestData;
import com.xsx.ncd.repository.TestDataRepository;
import com.xsx.ncd.spring.SpringFacktory;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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
	@FXML Label cvLabel1;
	@FXML Label cvLabel2;
	@FXML Label cvLabel3;
	@FXML Label cvLabel4;
	@FXML Label cvLabel5;
	@FXML Label cvLabel6;
	@FXML Label cvLabel7;
	@FXML Label cvLabel8;
	@FXML Label cvLabel9;
	@FXML Label cvLabel10;
	@FXML Label cvLabel11;
	@FXML Label cvLabel12;
	@FXML Label cvLabel13;
	@FXML Label cvLabel14;
	@FXML Label cvLabel15;
	@FXML Label cvLabel16;
	@FXML Label cvLabel17;
	@FXML Label cvLabel18;
	@FXML Label cvLabel19;
	@FXML Label cvLabel20;
	private Label[] cvLabels = new Label[20];
	
	private TestDataRepository testDataRepository;
	private ObjectMapper mapper = new ObjectMapper();
	private JavaType javaType = null;
	private List<Double> seriesdata = new ArrayList<>();
	private List<Double> cvDatas = new ArrayList<>();				//每隔calnum个点的cv值
	private Double[] cvDatasOfTC = new Double[4];			//TC线前后的CV值
	private Series<Number, Number> series1 = new Series<>();
	private int size = 0;
	private double tempDouble1 = 0;
	
	private final int calnum = 15;
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
        
        cvLabels[0] = cvLabel1;
        cvLabels[1] = cvLabel2;
        cvLabels[2] = cvLabel3;
        cvLabels[3] = cvLabel4;
        cvLabels[4] = cvLabel5;
        cvLabels[5] = cvLabel6;
        cvLabels[6] = cvLabel7;
        cvLabels[7] = cvLabel8;
        cvLabels[8] = cvLabel9;
        cvLabels[9] = cvLabel10;
        cvLabels[10] = cvLabel11;
        cvLabels[11] = cvLabel12;
        cvLabels[12] = cvLabel13;
        cvLabels[13] = cvLabel14;
        cvLabels[14] = cvLabel15;
        cvLabels[15] = cvLabel16;
        cvLabels[16] = cvLabel17;
        cvLabels[17] = cvLabel18;
        cvLabels[18] = cvLabel19;
        cvLabels[19] = cvLabel20;
        myChart.getData().addAll(series1);
        myChart.setAnimated(false);
        
        
 
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
		int i=0, j=0;
		List<Double> tempd = null;
		Integer t, b, c;
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
		
		testDataRepository = SpringFacktory.getCtx().getBean(TestDataRepository.class);
		javaType = mapper.getTypeFactory().constructParametricType(List.class, Double.class);
		
		rownum = sheet.getRows();

		for(j=0; j<2550; j++) {
			TestData testData = testDataRepository.findOne(j+1);
	        
	        if(testData != null){
	        	seriesdata.clear();
	        	cvDatas.clear();
	        	series1.getData().clear();

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
					seriesdata.clear();
					
					return;
				}
		        
		        //显示原始曲线，且计算cv值
		        t = testData.getT_l();
		        b = testData.getB_l();
		        c = testData.getC_l();
		        size = seriesdata.size();
		        for(i=0; i<size; i++){

		        	Data<Number, Number> data = new Data<Number, Number>(i, seriesdata.get(i));

		        	series1.getData().add(data);
		        	StackPane stackPane = (StackPane) data.getNode();
		        	stackPane.setPrefSize(1, 1);
		        	if((t != null) && (i == t.intValue())){
		        		stackPane.setStyle("-fx-background-color:red");
		        		stackPane.setPrefSize(10, 10);
		        		
		        		try {
		        			tempd = seriesdata.subList(i-15, i);
			        		tempDouble1 = getDataCV(tempd);
			        		cvDatasOfTC[0] = tempDouble1;
			        		
			        		tempd = seriesdata.subList(i, i+15);
			        		tempDouble1 = getDataCV(tempd);
			        		cvDatasOfTC[1] = tempDouble1;

						} catch (Exception e) {
							e.printStackTrace();
							// TODO: handle exception
						}
		        	}
		        	else if((b != null) && (i == b.intValue())){
		        		stackPane.setStyle("-fx-background-color:green");
		        		stackPane.setPrefSize(10, 10);
		        	}
		        	else if((c != null) && (i == c.intValue())){
		        		stackPane.setStyle("-fx-background-color:blue");
		        		stackPane.setPrefSize(10, 10);
		        		
		        		try {
		        			tempd = seriesdata.subList(i-15, i);
			        		tempDouble1 = getDataCV(tempd);
			        		cvDatasOfTC[2] = tempDouble1;
			        		
			        		tempd = seriesdata.subList(i, i+15);
			        		tempDouble1 = getDataCV(tempd);
			        		cvDatasOfTC[3] = tempDouble1;

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
		        	}
		        	
		        	//每隔calnum个点cv值
		        	if(i % calnum == 0) {
		        		try {
		        			tempd = seriesdata.subList(i, i+calnum);
			        		tempDouble1 = getDataCV(tempd);
			        		cvDatas.add(tempDouble1);
			        		
			        		cvLabels[i/calnum].setText(String.format("%.4f", tempDouble1));
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
		        	}
				}

		        try {
		        	
		        	rownum++;
				    jxl.write.Label pihaoLabel = new jxl.write.Label(0, rownum, String.format("%s-%s", testData.getCid(), testData.getCnum())); 
			    	
						sheet.addCell(pihaoLabel);
					
			    	 
				    for(i=0; i<cvDatas.size(); i++) {
				    	 number = new jxl.write.Number(i+1, rownum, cvDatas.get(i)); 
				    	 sheet.addCell(number);
				    }
				    
				    for(i=0; i<cvDatasOfTC.length; i++) {
					    number = new jxl.write.Number(i+21, rownum, cvDatasOfTC[i]); 
				    	 sheet.addCell(number);
				    }
				    
				    number = new jxl.write.Number(25, rownum, t); 
			    	 sheet.addCell(number);
			    	 
			    	 number = new jxl.write.Number(26, rownum, c); 
			    	 sheet.addCell(number);
			    	 
		        } catch (RowsExceededException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (WriteException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
		    	 
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
		}
		
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
       
		System.exit(0);
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
}
