package com.wzr.dao.entity.singlexmo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;


/**
 * Created by user on 2017/4/15.
 * 订单明细表格
 */
public class SingleXlsxManuOrder {
    private String singleXlsxFilename;


    private ArrayList<SingleSheet> singleSheetList;

    public ArrayList<SingleSheet> getSingleSheetList() {
        return singleSheetList;
    }

    public void setSingleSheetList(ArrayList<SingleSheet> singleSheetList) {
        this.singleSheetList = singleSheetList;
    }

    public String getSingleXlsxFilename() {
        return singleXlsxFilename;
    }

    public void setSingleXlsxFilename(String singleXlsxFilename) {
        this.singleXlsxFilename = singleXlsxFilename;
    }

    /**
     * 订单明细文件的构造函数
     *
     * @param xlsxFilename 订单明细文件的（全路径）文件名
     */
    public SingleXlsxManuOrder(String xlsxFilename) {
        this.setSingleXlsxFilename(xlsxFilename);
        if(this.getSingleSheetList() == null){
            this.singleSheetList = new ArrayList<>();
        }

        setSingleXlsxFields(xlsxFilename);
    }

    /**
     * 取指定订单文件的有效内容，放入相关字段中
     * @param singleXlsxFilename  订单文件的全路径名
     * @return 成功则返回true
     */
    private Boolean setSingleXlsxFields(String singleXlsxFilename) {
        try {
            InputStream sigleFile = new FileInputStream(singleXlsxFilename);
            // 工作薄对象
            Workbook workbook = WorkbookFactory.create(sigleFile);
            Iterator<Sheet> iteratorSheet = workbook.sheetIterator();
            while (iteratorSheet.hasNext()){
                // 工作表对象
                Sheet sheet = (Sheet) iteratorSheet.next();
                SingleSheet singleSheet = new SingleSheet(sheet);
                // 添加一个工作表
                this.singleSheetList.add(singleSheet);
            }

            sigleFile.close();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }


}
