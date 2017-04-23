package com.wzr.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;

public class MyXlsxView extends AbstractXlsxStreamingView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		String postInfo = req.getParameter("postInfo");
		JSONObject joPostInfo = new JSONObject(postInfo);

		// 文件名
		String filename = joPostInfo.getString("filenameSaveAs") + ".xlsx"; // 设置下载时客户端Excel的名称
		fillWorkbook(workbook, joPostInfo);

		//处理中文文件名
		String reqCharset = req.getCharacterEncoding(); /*根据request的getCharacterEncoding得到请求时的编码*/  
		filename = new String(filename.getBytes(reqCharset), "ISO8859-1");   
		resp.setCharacterEncoding(reqCharset);
		// 若想下载时自动填好文件名，则需要设置响应头的"Content-disposition"
		resp.setHeader("Content-disposition", "attachment;filename=" + filename);
	}

	private void fillWorkbook(Workbook workbook, JSONObject joPostInfo) {
		// 标题栏
		SXSSFWorkbook sxssWb = (SXSSFWorkbook)workbook;
		Sheet sheet = sxssWb.createSheet("sheet1");
		int iniRow = 0;
		Row row = null;
		Cell cell = null;
		if(joPostInfo.has("exportField")){
			// 含有标题栏
			JSONArray jaTableField = joPostInfo.getJSONArray("exportField");
			JSONArray jaOneField = null;
			if(jaTableField.length() > 0){
				// 标题不为空
				row = sheet.createRow((short) iniRow);
				// 添加表格标题行
				for(int i = iniRow; i < jaTableField.length(); ++i){
					cell = row.createCell(i);
					jaOneField = jaTableField.getJSONArray(i);
					cell.setCellType(SXSSFCell.CELL_TYPE_STRING); 
					cell.setCellValue(jaOneField.getString(1));
				}
				++iniRow; // 标题行完成，下移一行
			}
			// 商品内容
			JSONArray sc = joPostInfo.getJSONArray("exportContent");
			// 商品的一行
			JSONObject jsOneRow = null;
			// 添加商品内容
			for(int pdRow = iniRow; pdRow < sc.length() + iniRow; ++pdRow){
				row = sheet.createRow(pdRow);	// 在指定行创建一个Row对象
				jsOneRow = sc.getJSONObject(pdRow-iniRow);
				for(int col = 0; col < jaTableField.length(); ++col){
					// 循环填充一行的各列
					cell = row.createCell(col);
					cell.setCellType(SXSSFCell.CELL_TYPE_STRING);
					// 取当前列的字段信息
					jaOneField = jaTableField.getJSONArray(col);
					// 以字段代码为KEY,取JSONObject对象的值
					cell.setCellValue(jsOneRow.getString(jaOneField.getString(0)));
				}
//				cell = row.createCell(col++);
//				cell.setCellValue(jsL.getString("styleCode"));
//				cell = row.createCell(col++);
//				cell.setCellValue(jsL.getString("pdName")); // 品名
//				cell = row.createCell(col++);
//				cell.setCellValue(jsL.getInt("length")); // 长度
//				cell = row.createCell(col++);
//				cell.setCellValue(jsL.getString("size")); // 尺码
//				cell = row.createCell(col++);
//				cell.setCellValue(jsL.getString("cusCode")); // 客户代码
//				cell = row.createCell(col++);
//				cell.setCellValue(jsL.getString("billNo")); // 单号
//				cell = row.createCell(col++);
//				cell.setCellValue(jsL.getString("status")); // 商品状态
				
			}
		}else{
			// 出错
		}
		
	}
	
	 
}
