package com.wzr.dao.entity.singlexmo;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;

/**
 * Created by user on 2017/4/17.
 * 订单工作簿的一个工作表
 */
public class SingleSheet {
    

    private ArrayList<DetailRow> detailRows;

    public ArrayList<DetailRow> getDetailRows() {
        return detailRows;
    }


    public SingleSheet(Sheet xlsxSheet) {
        if(this.getDetailRows() == null){
            this.detailRows = new ArrayList<>();
        }

        addDetailRows(xlsxSheet);
    }

    /**
     * 通过读取sheet向this.detailRows中填充内容
     *
     * @param sheet 订单明细工作簿中的一个工作表
     */
    private void addDetailRows(Sheet sheet) {
        Row row;
        Cell xCell;
        for (int rowNo = 1; rowNo < sheet.getPhysicalNumberOfRows(); rowNo++) {
            row = sheet.getRow(rowNo);
            int colNo = 0;  // 列号
            xCell = row.getCell(colNo); // 取编号列
            if (xCell == null || xCell.getStringCellValue().isEmpty()) {
                // 款号为空，有效行结束
                break;
            } else {
                // 款号不为空，此行有效
                DetailRow detailRow = new DetailRow();
                xCell = row.getCell(colNo++);   // 编号
                detailRow.setBarCode(xCell.getStringCellValue());
                xCell = row.getCell(colNo++);	// 款号
                detailRow.setStyleCode(xCell.getStringCellValue()); 
                xCell = row.getCell(colNo++);   // 颜色
                detailRow.setPdName(xCell.getStringCellValue());
                xCell = row.getCell(colNo++);   // 长度
                if (xCell != null) {
                	if(xCell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                		detailRow.setLength(String.valueOf((int)xCell.getNumericCellValue()));
                	}
                	else{
                		detailRow.setLength(xCell.getStringCellValue());
                	}
                	
                }
                xCell = row.getCell(colNo++);   // 尺码
                if(xCell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                	detailRow.setSize(String.valueOf((int)xCell.getNumericCellValue()));
                }
                else{
                	detailRow.setSize(xCell.getStringCellValue());
                }
                xCell = row.getCell(colNo++);   // 客户
                if (xCell != null) {
                    detailRow.setCusCode(xCell.getStringCellValue());
                }
                xCell = row.getCell(colNo++);   // 单号
                detailRow.setBillNo(xCell.getStringCellValue());
                xCell = row.getCell(colNo++);   // 备注
                detailRow.setRemark((null == xCell)? "" : xCell.getStringCellValue());
                this.detailRows.add(detailRow);    // 加入有效行列表
            }

        }
    }
}
