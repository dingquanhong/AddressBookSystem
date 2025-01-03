package Utils.ExcelTool;

import Model.Contact;
import Model.Group;
import Model.User;
import Service.ContactService;
import Service.GroupService;
import Utils.Database.Result;
import Utils.UserSecssion.UserSecssion;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class ExcelFacade {
        public  String getCellValueAsString(Cell cell) {
            if (cell == null) {
                return "";
            }
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    } else {
                        return String.valueOf((long) cell.getNumericCellValue());
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    return cell.getCellFormula();
                default:
                    return "";
            }
        }
        public Result readExcel(String filePath) {
            try {
                File excelFile = new File(filePath);
                FileInputStream fileInputStream = new FileInputStream(excelFile);
                Workbook workbook = null;
                if (filePath.endsWith(".xls")) {
                    workbook = new HSSFWorkbook(fileInputStream);
                } else if (filePath.endsWith(".xlsx")) {
                    workbook = new XSSFWorkbook(fileInputStream);
                } else {
                    return new Result(false, "文件格式错误");
                }
                Sheet sheet = workbook.getSheetAt(0);
                int rowNum = sheet.getPhysicalNumberOfRows();
                if (rowNum == 0) {
                    return new Result(false, "文件为空");
                }

                ArrayList<ExcelContact> excelContacts = new ArrayList<>();

                for (int i = 0; i < rowNum; i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }

                    String groupname = getCellValueAsString(row.getCell(0));
                    String name = getCellValueAsString(row.getCell(1));
                    String sex = getCellValueAsString(row.getCell(2));
                    String phone = getCellValueAsString(row.getCell(3));
                    String email = getCellValueAsString(row.getCell(4));
                    String workunit = getCellValueAsString(row.getCell(5));
                    String address = getCellValueAsString(row.getCell(6));
                    String notes = getCellValueAsString(row.getCell(7));

                    ExcelContact excelContact = new ExcelContact(
                            -1,
                            UserSecssion.getSecssion().getUser().getId(),
                            -1,
                            name,
                            "男".equals(sex) ? 1 : 0,
                            phone,
                            email,
                            workunit,
                            address,
                            notes,
                            groupname
                    );
                    excelContacts.add(excelContact);
                }

                return new Result(true, "读取成功", excelContacts);
            } catch (IOException e) {
                return new Result(false, e.getMessage());
            }
        }
        public Result saveContactsToDatabase(ArrayList<ExcelContact> excelContacts) {
            GroupService groupService = new GroupService();
            ContactService contactService = new ContactService();
            User user = UserSecssion.getSecssion().getUser();
            for (ExcelContact excelContact : excelContacts){
                Result searchresult = groupService.getUserGroupByName(excelContact.getGroupName(),user);
                Group group = (Group) searchresult.getData();
                if (searchresult.getStatus()){
                    if (group==null){
                        Result addGroupResult = groupService.addUserGroup(user.getId(),excelContact.getGroupName());
                        if (addGroupResult.getStatus()){
                            group = (Group) groupService.getUserGroupByName(excelContact.getGroupName(),user).getData();
                        }else {
                            return new Result(false,addGroupResult.getMessage());
                        }
                    }
                    Result addContactResult = contactService.addContact(new Contact(
                            -1,
                            user.getId(),
                            group.getId(),
                            excelContact.getName(),
                            excelContact.getSex(),
                            excelContact.getPhone(),
                            excelContact.getEmail(),
                            excelContact.getWorkunit(),
                            excelContact.getAddress(),
                            excelContact.getNotes()
                    ));
                    if (!addContactResult.getStatus()){
                        return new Result(false,addContactResult.getMessage());
                    }

                }else {
                    return new Result(false,searchresult.getMessage());
                }
            }
            return new Result(true,"导入成功");
        }
        public Result exportExcel(ArrayList<Contact> contacts, String filePath) {
            Workbook workbook = null;
            if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook();
            } else if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook();
            } else {
                return new Result(false, "文件格式错误");
            }
            Sheet sheet = workbook.createSheet("LinkUp");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("序号");
            row.createCell(1).setCellValue("组别");
            row.createCell(2).setCellValue("姓名");
            row.createCell(3).setCellValue("性别");
            row.createCell(4).setCellValue("电话");
            row.createCell(5).setCellValue("邮箱");
            row.createCell(6).setCellValue("工作单位");
            row.createCell(7).setCellValue("住址");
            row.createCell(8).setCellValue("备注");
            for (int i = 0; i < contacts.size(); i++) {
                Contact contact = contacts.get(i);
                row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(i + 1);
                GroupService groupService = new GroupService();
                Result groupResult = groupService.getGroupByID(contact.getGroupId());
                if (groupResult.getStatus()){
                    row.createCell(1).setCellValue(((Group) groupResult.getData()).getGroupName());
                }else {
                    row.createCell(1).setCellValue("");
                }
                row.createCell(2).setCellValue(contact.getName());
                row.createCell(3).setCellValue(contact.getSex() == 1 ? "男" : "女");
                row.createCell(4).setCellValue(contact.getPhone());
                row.createCell(5).setCellValue(contact.getEmail());
                row.createCell(6).setCellValue(contact.getWorkunit());
                row.createCell(7).setCellValue(contact.getAddress());
                row.createCell(8).setCellValue(contact.getNotes());
            }
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
                return new Result(true, "导出成功");
            } catch (IOException e) {
                return new Result(false, e.getMessage());
            }
        }
}

