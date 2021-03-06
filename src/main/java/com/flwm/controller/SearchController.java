package com.flwm.controller;

import com.flwm.common.VO.ProfitVO;
import com.flwm.common.VO.SearchVO;
import com.flwm.common.VO.ShapeVO;
import com.flwm.common.VO.TechVO;
import com.flwm.common.annotation.AuthMember;
import com.flwm.common.domain.FMErrorEnum;
import com.flwm.common.domain.FMException;
import com.flwm.common.domain.Result;
import com.flwm.common.domain.SearchRequest;
import com.flwm.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by zhoupj on 10/29/18.
 */
@RestController
@RequestMapping(value = "/search", method = RequestMethod.POST)
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/list")
    public Result search(@RequestBody SearchRequest request) {

        Assert.isTrue(StringUtils.isNotBlank(request.getTradeDate()), "日期必须填写");

        Long total = searchService.searchCount(request);

        if (total > 100) {
            throw new FMException(FMErrorEnum.SEARCH_TOO_MANY);
        }

        List<SearchVO> list = searchService.search(request);

        return Result.succ(list, total);
    }

    @PostMapping("/k")
    @AuthMember
    public List<ShapeVO> queryK(@RequestParam(value = "code") String code) {
        return searchService.queryShape(code, 500);
    }

    @PostMapping("/basic")
    @AuthMember
    public Map<String, List<ProfitVO>> queryBasic(@RequestParam(value = "code") String code) {
        return searchService.queryBasicInfo(code, 12);
    }

    @PostMapping("/tech")
    @AuthMember
    public List<TechVO> queryTech(@RequestParam(value = "code") String code) {
        return searchService.queryTechData(code, 60);
    }

    @PostMapping("/org")
    @AuthMember
    public Map<String, Object> queryOrg(@RequestParam(value = "code") String code) {
        return searchService.queryOrgTrend(code, 10, 60);
    }


}
