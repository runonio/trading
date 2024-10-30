package io.runon.trading.data.investor;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 한국 시장의 기본투자자 매매동향 정보
 * 금융투자 (증권사, 자산운용사가 본인자산 운용)
 * 투신 (투자신탁 증권사, 자산운용사들이 고객이 맡긴 자금으로 운용)
 * 보험
 * 은행
 * 기타금융
 * 연기금 (연금, 기금 및 공제회, 국가/지자체, 국제기구, 기타법인중 공공기관)
 * ----------- 기관계
 * 기타법인 (금융회사가 아닌 기업의 운용, 자사주매입등
 * 개인
 * 외국인
 * @author macle
 */
@Data
public class InvestorDaily {

    Long t ;
    int ymd;

    //금융투자
    BigDecimal financial;

    //보험


    //투신




    //기타금융

    //연기금등

    //-------위에까지 기관합계

    //기타법인

    //개인

    //외국인



}
