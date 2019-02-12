package main;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import utils.HttpClientUtil2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author
 * @create 2018-02-24 15:18
 **/
public class testss {

    private static String ENCODE = "UTF-8";
    public static final int cache = 10 * 1024;
    public static final boolean isWindows;
    public static final String splash;
    public static final String root;

    static {
        if (System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().contains("windows")) {
            isWindows = true;
            splash = "\\";
            root = "D:";
        } else {
            isWindows = false;
            splash = "/";
            root = "/search";
        }
    }

    /**
     * 根据url下载文件，文件名从response header头中获取
     *
     * @param url
     * @return
     */
    //    public static String download(String url) {
    //        return download(url, null);
    //    }

    /**
     * 根据url下载文件，保存到filepath中
     *
     * @param url
     * @param filepath
     * @return
     */
    public static String download(String url, String filepath, String temp, String name) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = client.execute(httpget);

            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            if (filepath == null) {
                filepath = getFilePath(response);
            }
            File file = new File(filepath);
            file.getParentFile().mkdirs();
            FileOutputStream fileout = new FileOutputStream(file);
            byte[] buffer = new byte[cache];
            int ch = 0;
            while ((ch = is.read(buffer)) != -1) {
                fileout.write(buffer, 0, ch);
            }
            is.close();
            fileout.flush();
            fileout.close();

            if (temp.contains(".zip")) {
                File ff = new File(filepath);
                ZipFile zip = new ZipFile(ff,Charset.forName("gbk"));
                for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements(); ) {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    String zipEntryName = temp.replace(".zip","");
                    InputStream in = zip.getInputStream(entry);
                    String outPath = ("D:\\test2\\" + zipEntryName).replaceAll("\\*", "/");

                    // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                    if (new File(outPath).isDirectory()) {
                        continue;
                    }
                    // 输出文件路径信息
                    System.out.println(outPath);

                    FileOutputStream out = new FileOutputStream(outPath);
                    byte[] buf1 = new byte[1024];
                    int len;
                    while ((len = in.read(buf1)) > 0) {
                        out.write(buf1, 0, len);
                    }
                    in.close();
                    out.close();
                }
                System.out.println("******************解压完毕********************");

            }


            //            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取response要下载的文件的默认路径
     *
     * @param response
     * @return
     */
    public static String getFilePath(HttpResponse response) {
        String filepath = root + splash;
        String filename = getFileName(response);

        if (filename != null) {
            filepath += filename;
        } else {
            filepath += getRandomFileName();
        }
        return filepath;
    }

    /**
     * 获取response header中Content-Disposition中的filename值
     *
     * @param response
     * @return
     */
    public static String getFileName(HttpResponse response) {
        Header contentHeader = response.getFirstHeader("Content-Disposition");
        String filename = null;
        if (contentHeader != null) {
            HeaderElement[] values = contentHeader.getElements();
            if (values.length == 1) {
                NameValuePair param = values[0].getParameterByName("filename");
                if (param != null) {
                    try {
                        //filename = new String(param.getValue().toString().getBytes(), "utf-8");
                        //filename=URLDecoder.decode(param.getValue(),"utf-8");
                        filename = param.getValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return filename;
    }

    /**
     * 获取随机文件名
     *
     * @return
     */
    public static String getRandomFileName() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static void outHeaders(HttpResponse response) {
        Header[] headers = response.getAllHeaders();
        for (int i = 0; i < headers.length; i++) {
            System.out.println(headers[i]);
        }
    }

    public static void main(String[] args) {
        //      String url = "http://bbs.btwuji.com/job.php?action=download&pid=tpc&tid=320678&aid=216617";


        Map<Integer, String> nameMap = new HashMap();
        nameMap.put(126367,"入驻合同-袁付-20180808_823515337993457395809.pdf");
        nameMap.put(126368,"入驻合同-袁付-20180808_8235_img15337993457395809.pdf");
        nameMap.put(126499,"入驻合同-袁付-20180810_828215338655429756030.pdf");
        nameMap.put(126500,"入驻合同-袁付-20180810_8282_img15338655429756030.pdf");
        nameMap.put(126805,"深流远景合同2055.pdf");
        nameMap.put(126806,"WechatIMG32.jpeg");
        nameMap.put(126886,"入驻合同-袁付-20180808_823115341235282630402.pdf");
        nameMap.put(126887,"入驻合同-袁付-20180808_8231_img15341235282630402.pdf");
        nameMap.put(127919,"2018-08-12 09-50.pdf");
        nameMap.put(127920,"2018-08-12 09-50 第1页.pdf");
        nameMap.put(128631,"入驻合同-张宝银-20180819_843215346469388112850.pdf");
        nameMap.put(128632,"入驻合同-张宝银-20180819_8432_img15346469388112850.pdf");
        nameMap.put(129157,"入驻合同-闫赛-20180820_847715348222787141619.pdf");
        nameMap.put(129158,"入驻合同-闫赛-20180820_8477_img15348222787141619.pdf");
        nameMap.put(129183,"20180821115231-0001.pdf");
        nameMap.put(129781,"入驻合同-张连正-20180823_854315349975207632051.pdf");
        nameMap.put(129782,"入驻合同-张连正-20180823_8543_img15349975207632051.pdf");
        nameMap.put(130502,"入驻合同-李俄辉-20180824_858415353469033198767.pdf");
        nameMap.put(130503,"入驻合同-李俄辉-20180824_8584_img15353469033198767.pdf");
        nameMap.put(130601,"入驻合同-张连正-20180827_862115353570855701790.pdf");
        nameMap.put(130603,"入驻合同-张连正-20180827_8621_img15353570855701790.pdf");
        nameMap.put(130609,"入驻合同-李俄辉-20180827_862315353573631164113.pdf");
        nameMap.put(130610,"入驻合同-李俄辉-20180827_8623_img15353573631164113.pdf");
        nameMap.put(130802,"入驻合同-王雪-20180827_863915353661351673952.pdf");
        nameMap.put(130803,"入驻合同-王雪-20180827_8639_img15353661351673952.pdf");
        nameMap.put(130943,"宁波达特海峡2016房间.pdf");
        nameMap.put(131072,"b北京源型悦雅教育咨询有限公司.pdf");
        nameMap.put(131823,"20180830094441-0001.pdf");
        nameMap.put(131824,"20180830095746-0001.pdf");
        nameMap.put(132477,"入驻合同-陈海利-20180831_874015356845814612480.pdf");
        nameMap.put(132478,"入驻合同-陈海利-20180831_8740_img15356845814612480.pdf");
        nameMap.put(132648,"入驻合同-王雪-20180831_875115357010149197046.pdf");
        nameMap.put(132649,"入驻合同-王雪-20180831_8751_img15357010149197046.pdf");
        nameMap.put(132735,"海峡2029承诺函.pdf");
        nameMap.put(132736,"海峡2029客户会员协议.pdf");
        nameMap.put(132737,"久泰蓝山营业执照副本（新）(1).pdf");
        nameMap.put(133490,"入驻合同-崔琛-20180829_869115360496657838231.pdf");
        nameMap.put(133491,"入驻合同-崔琛-20180829_8691_img15360496657838231.pdf");
        nameMap.put(133948,"新文档 2018-09-04 17.12.33_20180904171322.pdf");
        nameMap.put(134308,"PINGO SPACE合同.pdf");
        nameMap.put(134315,"PINGO SPACE 营业执照.jpg");
        nameMap.put(135349,"入驻合同-高珊珊-20180907_892515366356578658173.pdf");
        nameMap.put(135350,"入驻合同-高珊珊-20180907_8925_img15366356578658173.pdf");
        nameMap.put(135363,"入驻合同-王晶-20180911_899415366361619988832.pdf");
        nameMap.put(135364,"入驻合同-王晶-20180911_8994_img15366361619988832.pdf");
        nameMap.put(135495,"续租合同-王晶-20180911_900615366482777304882.pdf");
        nameMap.put(135496,"续租合同-王晶-20180911_9006_img15366482777304882.pdf");
        nameMap.put(135514,"续租合同-王晶-20180911_900815366496755218609.pdf");
        nameMap.put(135515,"续租合同-王晶-20180911_9008_img15366496755218609.pdf");
        nameMap.put(135574,"深流远景续租.pdf");
        nameMap.put(135603,"入驻合同-张连正-20180905_884115366572541069342.pdf");
        nameMap.put(135604,"入驻合同-张连正-20180905_8841_img15366572541069342.pdf");
        nameMap.put(135688,"海峡国际2052.pdf");
        nameMap.put(136740,"20180914光大信托扫描件.pdf");
        nameMap.put(136777,"入驻合同-李俄辉-20180915_911215369853939041561.pdf");
        nameMap.put(136778,"入驻合同-李俄辉-20180915_9112_img15369853939041561.pdf");
        nameMap.put(136780,"2023扫描.pdf");
        nameMap.put(137153,"续租合同-陈清-20180918_912915372363891373958.pdf");
        nameMap.put(137154,"续租合同-陈清-20180918_9129_img15372363891373958.pdf");
        nameMap.put(137551,"入驻合同-张志宽-20180907_894315373275050347331.pdf");
        nameMap.put(137552,"入驻合同-张志宽-20180907_8943_img15373275050347331.pdf");
        nameMap.put(138583,"京诚科来续租.pdf");
        nameMap.put(138723,"楚合同.pdf");
        nameMap.put(139148,"氪空间·中化资本入驻协议.pdf");
        nameMap.put(139149,"中化资本营业执照.pdf");
        nameMap.put(139277,"入驻合同-张连正-20180925_926815378619702497092.pdf");
        nameMap.put(139278,"入驻合同-张连正-20180925_9268_img15378619702497092.pdf");
        nameMap.put(139500,"海峡社区光大信托会员补充协议.pdf");
        nameMap.put(139888,"入驻合同-李林-20180927_932815380189558573992.pdf");
        nameMap.put(139889,"入驻合同-李林-20180927_9328_img15380189558573992.pdf");
        nameMap.put(139910,"海峡移动006合同.pdf");
        nameMap.put(139975,"入驻合同-李俄辉-20180927_933615380343693130526.pdf");
        nameMap.put(139976,"入驻合同-李俄辉-20180927_9336_img15380343693130526.pdf");
        nameMap.put(139992,"入驻合同-李俄辉-20180927_933915380359722164447.pdf");
        nameMap.put(139993,"入驻合同-李俄辉-20180927_9339_img15380359722164447.pdf");
        nameMap.put(140128,"海峡何占金18928.pdf");
        nameMap.put(141732,"续租合同-王晶-20181010_948715391393152266363.pdf");
        nameMap.put(141733,"续租合同-王晶-20181010_9487_img15391393152266363.pdf");
        nameMap.put(142035,"入驻合同-张连正-20181011_951915392245866896717.pdf");
        nameMap.put(142036,"入驻合同-张连正-20181011_9519_img15392245866896717.pdf");
        nameMap.put(142043,"北京翼翕体育股份有限公司.pdf");
        nameMap.put(142492,"海峡国际社区2055续租合同20181012.pdf");
        nameMap.put(145705,"入驻合同-张连正-20181029_993015407852863604415.pdf");
        nameMap.put(145706,"入驻合同-张连正-20181029_9930_img15407852863604415.pdf");
        nameMap.put(145730,"入驻合同-魏思琪-20181029_992915407925983299368.pdf");
        nameMap.put(145731,"入驻合同-魏思琪-20181029_9929_img15407925983299368.pdf");
        nameMap.put(145965,"入驻合同-张玥莹-20181030_997115408679817796001.pdf");
        nameMap.put(145966,"入驻合同-张玥莹-20181030_9971_img15408679817796001.pdf");
        nameMap.put(145967,"入驻合同-高珊珊-20181029_994815408680942382175.pdf");
        nameMap.put(145968,"入驻合同-高珊珊-20181029_9948_img15408680942382175.pdf");
        nameMap.put(146312,"入驻合同-徐诚-20181030_1000915409021977143720.pdf");
        nameMap.put(146313,"入驻合同-徐诚-20181030_10009_img15409021977143720.pdf");
        nameMap.put(146617,"入驻合同-徐诚-20181031_1003215409759450534806.pdf");
        nameMap.put(146618,"入驻合同-徐诚-20181031_10032_img15409759450534806.pdf");
        nameMap.put(146766,"增租合同-张连正-20181031_1005815409945386196916.pdf");
        nameMap.put(146767,"增租合同-张连正-20181031_10058_img15409945386196916.pdf");
        nameMap.put(146789,"20181101101131-0001.pdf");
        nameMap.put(146790,"20181101101150-0001.pdf");
        nameMap.put(147089,"入驻合同-赵峥-20181102_1008415411247425049016.pdf");
        nameMap.put(147090,"入驻合同-赵峥-20181102_10084_img15411247425049016.pdf");
        nameMap.put(147838,"入驻合同-赵峥-20181105_1014915414246066537317.pdf");
        nameMap.put(147839,"入驻合同-赵峥-20181105_10149_img15414246066537317.pdf");
        nameMap.put(149084,"海峡5055会员协议.pdf");
        nameMap.put(149096,"续租合同-赵峥-20181109_1027115417384043522794.pdf");
        nameMap.put(149097,"续租合同-赵峥-20181109_10271_img15417384043522794.pdf");
        nameMap.put(149112,"深流远景续租.pdf");
        nameMap.put(149277,"北京海峡氪空间.pdf");
        nameMap.put(149544,"2033.pdf");
        nameMap.put(150514,"入驻合同-王超群-20181113_1035215420995392179981.pdf");
        nameMap.put(150515,"入驻合同-王超群-20181113_10352_img15420995392179981.pdf");
        nameMap.put(150533,"入驻合同-张志宽-20181106_1015915421007772242500.pdf");
        nameMap.put(150534,"入驻合同-张志宽-20181106_10159_img15421007772242500.pdf");
        nameMap.put(150699,"入驻合同-王超群-20181113_1035815421033725577973.pdf");
        nameMap.put(150700,"入驻合同-王超群-20181113_10358_img15421033725577973.pdf");
        nameMap.put(150750,"中智通.pdf");
        nameMap.put(150752,"营业执照-中智通.jpg");
        nameMap.put(151640,"入驻合同-赵峥-20181114_1037115421780679146397.pdf");
        nameMap.put(151641,"入驻合同-赵峥-20181114_10371_img15421780679146397.pdf");
        nameMap.put(152060,"1.jpg");
        nameMap.put(152063,"2.jpg");
        nameMap.put(152065,"3.jpg");
        nameMap.put(152067,"4.jpg");
        nameMap.put(152069,"5.jpg");
        nameMap.put(152071,"6.jpg");
        nameMap.put(152073,"7.jpg");
        nameMap.put(152074,"8.jpg");
        nameMap.put(152076,"9.jpg");
        nameMap.put(152078,"9.jpg");
        nameMap.put(152081,"11.jpg");
        nameMap.put(152082,"10.jpg");
        nameMap.put(152084,"身份证.jpg");
        nameMap.put(152087,"执照.jpg");
        nameMap.put(152968,"海峡中化保险经纪入驻协议签约版含营业执照.pdf");
        nameMap.put(154784,"入驻合同-高珊珊-20181126_1064515432271606934546.pdf");
        nameMap.put(154785,"入驻合同-高珊珊-20181126_10645_img15432271606934546.pdf");
        nameMap.put(154932,"减租合同-陈清-20181127_1066315432999996229973.pdf");
        nameMap.put(154933,"减租合同-陈清-20181127_10663_img15432999996229973.pdf");
        nameMap.put(154965,"思锐信达减租.pdf");
        nameMap.put(156079,"增租合同-高珊珊-20181201_1079715436356343500116.pdf");
        nameMap.put(156080,"增租合同-高珊珊-20181201_10797_img15436356343500116.pdf");
        nameMap.put(156315,"增租合同-高珊珊-20181203_1080215438189571887245.pdf");
        nameMap.put(156316,"增租合同-高珊珊-20181203_10802_img15438189571887245.pdf");
        nameMap.put(158363,"入驻合同-刘晓-20181210_1097115444211468249151.pdf");
        nameMap.put(158364,"入驻合同-刘晓-20181210_10971_img15444211468249151.pdf");
        nameMap.put(158772,"创业大街2020.pdf");
        nameMap.put(159003,"入驻合同-刘晓-20181211_1102115445128044103531.pdf");
        nameMap.put(159004,"入驻合同-刘晓-20181211_11021_img15445128044103531.pdf");
        nameMap.put(159516,"海峡国际社区2051.pdf");
        nameMap.put(160031,"入驻合同-高珊珊-20181203_1080615447735113262503.pdf");
        nameMap.put(160032,"入驻合同-高珊珊-20181203_10806_img15447735113262503.pdf");
        nameMap.put(160148,"入驻合同-徐诚-20181215_1115415448625688589123.pdf");
        nameMap.put(160149,"入驻合同-徐诚-20181215_11154_img15448625688589123.pdf");
        nameMap.put(160215,"入驻合同-高珊珊-20181217_1116215450143832100486.pdf");
        nameMap.put(160216,"入驻合同-高珊珊-20181217_11162_img15450143832100486.pdf");
        nameMap.put(160266,"入驻合同-王雪-20181205_1088915450161184003874.pdf");
        nameMap.put(160267,"入驻合同-王雪-20181205_10889_img15450161184003874.pdf");
        nameMap.put(160736,"陈馨.pdf");
        nameMap.put(160859,"入驻合同-刘晓-20181218_1122015451161499031160.pdf");
        nameMap.put(160860,"入驻合同-刘晓-20181218_11220_img15451161499031160.pdf");
        nameMap.put(161877,"2053.pdf");
        nameMap.put(162150,"入驻合同-刘晓-20181220_1128715453738302063085.pdf");
        nameMap.put(162151,"入驻合同-刘晓-20181220_11287_img15453738302063085.pdf");
        nameMap.put(162399,"20181224博动医学.pdf");
        nameMap.put(162801,"入驻合同-刘晓-20181225_1140215457199871261561.pdf");
        nameMap.put(162803,"入驻合同-刘晓-20181225_11402_img15457199871261561.pdf");
        nameMap.put(163521,"入驻合同-张连正-20181227_1148315458815049906947.pdf");
        nameMap.put(163522,"入驻合同-张连正-20181227_11483_img15458815049906947.pdf");
        nameMap.put(163549,"20181227东方优旭日.pdf");
        nameMap.put(163893,"东方优旭营业执照.pdf");
        nameMap.put(164678,"今日头条有限公司-入驻合同-20181229.pdf");
        nameMap.put(164849,"减租合同-王鹏-20190102_1162515463972450183007.pdf");
        nameMap.put(164850,"减租合同-王鹏-20190102_11625_img15463972450183007.pdf");
        nameMap.put(164851,"减租邮件说明.jpg");
        nameMap.put(166568,"入驻合同-刘晓-20190104_1170215468521317637931.pdf");
        nameMap.put(166569,"入驻合同-刘晓-20190104_11702_img15468521317637931.pdf");
        nameMap.put(167498,"增租合同-张连正-20190109_1181015470852327474626.pdf");
        nameMap.put(167499,"增租合同-张连正-20190109_11810_img15470852327474626.pdf");
        nameMap.put(167607,"入驻合同-刘晓-20190104_1172115471021998309800.pdf");
        nameMap.put(167608,"入驻合同-刘晓-20190104_11721_img15471021998309800.pdf");
        nameMap.put(169106,"续租合同-张春旺-20190116_1200515476095514286644.pdf");
        nameMap.put(169107,"续租合同-张春旺-20190116_12005_img15476095514286644.pdf");
        nameMap.put(169160,"正同金财.pdf");
        nameMap.put(169410,"北京字跳网络技术有限公司-入驻合同-20190107 (1).pdf");



        List<Integer> ids = new ArrayList<>();
        ids.add(126367);
        ids.add(126368);
        ids.add(126499);
        ids.add(126500);
        ids.add(126805);
        ids.add(126806);
        ids.add(126886);
        ids.add(126887);
        ids.add(127919);
        ids.add(127920);
        ids.add(128631);
        ids.add(128632);
        ids.add(129157);
        ids.add(129158);
        ids.add(129183);
        ids.add(129781);
        ids.add(129782);
        ids.add(130502);
        ids.add(130503);
        ids.add(130601);
        ids.add(130603);
        ids.add(130609);
        ids.add(130610);
        ids.add(130802);
        ids.add(130803);
        ids.add(130943);
        ids.add(131072);
        ids.add(131823);
        ids.add(131824);
        ids.add(132477);
        ids.add(132478);
        ids.add(132648);
        ids.add(132649);
        ids.add(132735);
        ids.add(132736);
        ids.add(132737);
        ids.add(133490);
        ids.add(133491);
        ids.add(133948);
        ids.add(134308);
        ids.add(134315);
        ids.add(135349);
        ids.add(135350);
        ids.add(135363);
        ids.add(135364);
        ids.add(135495);
        ids.add(135496);
        ids.add(135514);
        ids.add(135515);
        ids.add(135574);
        ids.add(135603);
        ids.add(135604);
        ids.add(135688);
        ids.add(136740);
        ids.add(136777);
        ids.add(136778);
        ids.add(136780);
        ids.add(137153);
        ids.add(137154);
        ids.add(137551);
        ids.add(137552);
        ids.add(138583);
        ids.add(138723);
        ids.add(139148);
        ids.add(139149);
        ids.add(139277);
        ids.add(139278);
        ids.add(139500);
        ids.add(139888);
        ids.add(139889);
        ids.add(139910);
        ids.add(139975);
        ids.add(139976);
        ids.add(139992);
        ids.add(139993);
        ids.add(140128);
        ids.add(141732);
        ids.add(141733);
        ids.add(142035);
        ids.add(142036);
        ids.add(142043);
        ids.add(142492);
        ids.add(145705);
        ids.add(145706);
        ids.add(145730);
        ids.add(145731);
        ids.add(145965);
        ids.add(145966);
        ids.add(145967);
        ids.add(145968);
        ids.add(146312);
        ids.add(146313);
        ids.add(146617);
        ids.add(146618);
        ids.add(146766);
        ids.add(146767);
        ids.add(146789);
        ids.add(146790);
        ids.add(147089);
        ids.add(147090);
        ids.add(147838);
        ids.add(147839);
        ids.add(149084);
        ids.add(149096);
        ids.add(149097);
        ids.add(149112);
        ids.add(149277);
        ids.add(149544);
        ids.add(150514);
        ids.add(150515);
        ids.add(150533);
        ids.add(150534);
        ids.add(150699);
        ids.add(150700);
        ids.add(150750);
        ids.add(150752);
        ids.add(151640);
        ids.add(151641);
        ids.add(152060);
        ids.add(152063);
        ids.add(152065);
        ids.add(152067);
        ids.add(152069);
        ids.add(152071);
        ids.add(152073);
        ids.add(152074);
        ids.add(152076);
        ids.add(152078);
        ids.add(152081);
        ids.add(152082);
        ids.add(152084);
        ids.add(152087);
        ids.add(152968);
        ids.add(154784);
        ids.add(154785);
        ids.add(154932);
        ids.add(154933);
        ids.add(154965);
        ids.add(156079);
        ids.add(156080);
        ids.add(156315);
        ids.add(156316);
        ids.add(158363);
        ids.add(158364);
        ids.add(158772);
        ids.add(159003);
        ids.add(159004);
        ids.add(159516);
        ids.add(160031);
        ids.add(160032);
        ids.add(160148);
        ids.add(160149);
        ids.add(160215);
        ids.add(160216);
        ids.add(160266);
        ids.add(160267);
        ids.add(160736);
        ids.add(160859);
        ids.add(160860);
        ids.add(161877);
        ids.add(162150);
        ids.add(162151);
        ids.add(162399);
        ids.add(162801);
        ids.add(162803);
        ids.add(163521);
        ids.add(163522);
        ids.add(163549);
        ids.add(163893);
        ids.add(164678);
        ids.add(164849);
        ids.add(164850);
        ids.add(164851);
        ids.add(166568);
        ids.add(166569);
        ids.add(167498);
        ids.add(167499);
        ids.add(167607);
        ids.add(167608);
        ids.add(169106);
        ids.add(169107);
        ids.add(169160);
        ids.add(169410);



        for (Integer id : ids) {
            Map<String, String> map1 = new HashMap<String, String>(4);
            map1.put("id", id.toString());
            String result = HttpClientUtil2.post("https://op.krspace.cn/api/krspace-op-web/sys/down-file", map1, true).getEntity();
            JSONObject rt = JSONObject.parseObject(result);
            //            list.add(rt.get("data").toString());
            String fileurl = rt.get("data").toString();
            String temp = nameMap.get(id);
            if (fileurl.contains(".zip")) {
                temp = temp + ".zip";
            } else {
            }
            String filepath = "D:\\test2\\" + temp;
            testss.download(fileurl, filepath, temp, temp);
            System.out.println(rt.get("data").toString());
        }
        System.out.println("end");
    }


}
