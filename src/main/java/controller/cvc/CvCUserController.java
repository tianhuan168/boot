package controller.cvc;

import com.alibaba.fastjson.JSONObject;
import entity.cvc.CvCUser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import service.cvc.CvCUserService;
import utils.JsonResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * user 控制器
 *
 * @author tianhuan 2018-12-23
 */
@RestController
@RequestMapping("cvc")
public class CvCUserController {
    private static final Logger logger = LoggerFactory.getLogger(CvCUserController.class);

    @Autowired
    private CvCUserService cvCUserService;

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public JsonResponse findAll() {
        List<CvCUser> list = cvCUserService.findAll();
        return JsonResponse.success(list);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public JsonResponse add(CvCUser user) {
        cvCUserService.save(user);
        return JsonResponse.success();
    }

    @RequestMapping(value = "import-user", method = RequestMethod.POST)
    public JsonResponse importUser(@RequestParam("file") MultipartFile file) {
        if (null == file || file.isEmpty()) {
            return JsonResponse.failed("请选择文件");
        }
        String fileNameEnd = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"xls".equals(fileNameEnd) && !"xlsx".equals(fileNameEnd)) {
            return JsonResponse.failed("文件格式错误");
        }

        List<String> datas = new ArrayList<String>();
        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = wb.getSheetAt(0);
            // 获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum();
            // 获得当前sheet的结束行
            int lastRowNum = sheet.getPhysicalNumberOfRows();
            // 循环除了第一行的所有行
            for (int rowNum = firstRowNum + 1; rowNum < lastRowNum; rowNum++) {
                // 获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                CvCUser user = new CvCUser();
                user.setName(row.getCell(0).toString());
                cvCUserService.save(user);
            }
            wb.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }


        return JsonResponse.success(JSONObject.toJSONString(datas));
    }

}
