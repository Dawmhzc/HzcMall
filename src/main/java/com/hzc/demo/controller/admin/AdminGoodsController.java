package com.hzc.demo.controller.admin;

import com.hzc.demo.commom.Result;
import com.hzc.demo.pojo.Goods;
import com.hzc.demo.service.CategoryService;
import com.hzc.demo.service.GoodsService;
import com.hzc.demo.service.PriceSurgeService;
import com.hzc.demo.util.CategoryMap;
import com.hzc.demo.util.IdCollection;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class AdminGoodsController {

    @Resource
    GoodsService goodsService;
    @Resource
    CategoryService categoryService;
    @Resource
    PriceSurgeService priceSurgeService;

    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        String filePath=this.getClass().getResource("/static/img").getPath();
        System.out.println(filePath);
        return "123";
    }

    /*@GetMapping("/getAllGoods")
    public String getAllGoods(Model model){
        List<Goods> goodsList=goodsService.findGoodsByLevel(new CategoryMap(),null);
        model.addAttribute("goodsList",goodsList);
        return "Admin/AdminGoods";
    }*/

    /*@GetMapping("/getGoodsDetail/{goodsId}")
    public String getGoodsDetail(@PathVariable("goodsId") Integer goodsId, Model model){
        model.addAttribute("goods",goodsService.getGoodsById(goodsId));
        return "Admin/vertical/app-ecommerce-product-detail";
    }*/

    @GetMapping("/goEditGoods/{goodsId}")
    public String goEditGoods(@PathVariable("goodsId") Integer goodsId,Model model){
        model.addAttribute("goods",goodsService.getGoodsById(goodsId));
        return "Admin/showGoods";
    }

    @GetMapping("/goAddGoods")
    public String goAddGoods(HttpServletRequest request){
        System.out.println("request.getServletPath():"+request.getServletPath());
        System.out.println("request.getContextPath():"+request.getContextPath());
        return "Admin/addGoods";
    }

    @PostMapping("/saveGoods")
    @ResponseBody
    public Result saveGoods(@Nullable @RequestParam("goodsName") String goodsName,
                            @Nullable @RequestParam("goodsIntro") String goodsIntro,
                            @Nullable @RequestParam("goodsCategoryId") Integer goodsCategoryId,
                            @Nullable @RequestParam("goodsImg") MultipartFile goodsImg,
                            @Nullable @RequestParam("originalPrice") Double originalPrice,
                            @Nullable @RequestParam("sellingPrice") Double sellingPrice,
                            @Nullable @RequestParam("stockNum") Integer stockNum,
                            @Nullable @RequestParam("createUser") Integer createUser) throws IOException, IOException {
        Goods goods=new Goods();
        goods.setGoodsName(goodsName);
        goods.setGoodsCategoryId(goodsCategoryId);
        //goods.setGoodsImg(goodsImg.getOriginalFilename());
        goods.setGoodsIntro(goodsIntro);
        goods.setOriginalPrice(originalPrice);
        goods.setStockNum(stockNum);
        goods.setSellStatus(0);
        goods.setSellingPrice(sellingPrice);
        goods.setCreateUser(createUser);
        if (Objects.isNull(goodsImg)) {
            System.out.println("没有传递图片文件");
            return new Result(null,1,"没有传递图片文件");
        }
        String fileName=goodsImg.getOriginalFilename();
        goods.setGoodsImg(fileName);
        Result result=goodsService.saveGoods(goods);

        if (result.getCode()==1) return result;
        Class<? extends AdminGoodsController> aClass = this.getClass();
        URL resource = aClass.getResource("/static/img");
        String filePath = resource.getPath();
       // String filePath=this.getClass().getResource("/static/img").getPath();
        filePath=filePath.substring(1);
        System.out.println("文件目录："+filePath);
        File dest=new File(filePath+"/"+fileName);
        goodsImg.transferTo(dest);

        Double[] array=new Double[2];
        array[0]=goods.getOriginalPrice();
        array[1]=goods.getSellingPrice();
        String surge= Strings.join(Arrays.asList(array),',');
        priceSurgeService.insertSurge(goodsService.getUserGoods(goodsName,goodsCategoryId,createUser).getGoodsId(),surge);
        return result;
    }

    @PostMapping("/updateGoods")
    @ResponseBody
    //前端没有传递除了String类型参数在@RequestParam获取参数时会终止返回underfind,使用pojo对象接受参数，参数不全可以生成对象但是调用时会抛出空指针异常
    public Result updateGoods(@Nullable @RequestParam("goodsId") Integer goodsId,
                              @Nullable @RequestParam("goodsName") String goodsName,
                              @Nullable @RequestParam("goodsIntro") String goodsIntro,
                              @Nullable @RequestParam("goodsCategoryId") Integer goodsCategoryId,
                              @Nullable @RequestParam("goodsImg") MultipartFile goodsImg,
                              @Nullable @RequestParam("originalPrice") Double originalPrice,
                              @Nullable @RequestParam("sellingPrice") Double sellingPrice,
                              @Nullable @RequestParam("stockNum") Integer stockNum,
                              @Nullable @RequestParam("createUser") Integer createUser) throws IOException {
        Goods goods=new Goods();
        goods.setGoodsId(goodsId);
        goods.setGoodsName(goodsName);
        goods.setGoodsCategoryId(goodsCategoryId);
        goods.setGoodsIntro(goodsIntro);
        goods.setOriginalPrice(originalPrice);
        goods.setStockNum(stockNum);
        goods.setSellStatus(0);
        goods.setSellingPrice(sellingPrice);
        goods.setCreateUser(createUser);
        File dest;
        if (!Objects.isNull(goodsImg)) {
            String fileName=goodsImg.getOriginalFilename();
            goods.setGoodsImg(fileName);
        }
        Result result=goodsService.editGoods(goods);
        if (result.getCode()==1) return result;

        priceSurgeService.updateSurge(""+sellingPrice,goodsId);
        if (!Objects.isNull(goodsImg)) {
            Class<? extends AdminGoodsController> aClass = this.getClass();
            URL resource = aClass.getResource("/static/img");
            String filePath = resource.getPath();
            //String filePath=this.getClass().getResource("/static/img").getPath();
            filePath=filePath.substring(1);
            System.out.println("文件目录："+filePath);
            dest=new File(filePath+"/"+goodsImg.getOriginalFilename());
            goodsImg.transferTo(dest);
        }
        return result;
    }

    @PostMapping("/delGoods")
    @ResponseBody
    public Result delGoods(IdCollection idCollection){
        return goodsService.delGoodsList(idCollection.getIds());
    }

    @PostMapping("/downGoods")
    @ResponseBody
    public Result downGoods(IdCollection idCollection){
        System.out.println(idCollection.getIds());
        return goodsService.downGoods(idCollection.getIds(),1);
    }

    @PostMapping("/upGoods")
    @ResponseBody
    public Result upGoods(IdCollection idCollection){
        System.out.println(idCollection.getIds());
        return goodsService.downGoods(idCollection.getIds(),0);
    }

    @RequestMapping("/getAllGoods")
    public String getGoodsByUser(Map<String,Object> map, HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null){
            map.put("message","未登录");
            return "Admin/auth-404";
        }
        Result result = goodsService.getGoodsByUser(userId);
        if (result.getCode() != 0){
            map.put("message",result.getMessage());
            return "Admin/auth-404";
        }
        List<Goods> list = (List<Goods>) result.getData();
        map.put("goodsList",list);
        return "Admin/AdminGoods";
    }
}
