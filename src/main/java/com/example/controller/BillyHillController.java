package com.example.controller;

import com.example.domain.BetInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class BillyHillController extends GeneralBookieController {
    private String bookie = "BillyHill";
    private double oddCalcValue = 0.778654;

    @RequestMapping(value = "/BillyHill", method = RequestMethod.GET)
    public String checkBookie(Model model, HttpSession session) {
        session.setAttribute("bookie", "BillyHill");
        return super.checkBookie(model, session, bookie);
    }

    @RequestMapping(value = "/BillyHill/Basketball", method = RequestMethod.GET)
    public String bookieGetBasketball(Model model) {
        return super.bookieGetBasketball(model, bookie);
    }

    @RequestMapping(value = "/BillyHill/Basketball/{hrefInfo}", method = RequestMethod.GET)
    public String bookieCheckBasketballOdds(@PathVariable("hrefInfo") String hrefInfo, Model model, HttpSession session) {
        return super.bookieCheckBasketballOdds(hrefInfo, model, session, bookie, oddCalcValue);
    }

    @RequestMapping(value = "/BillyHill/Basketball/placeBet", method = RequestMethod.POST)
    public String bookiePlaceBetBasketball(@ModelAttribute(value = "betinfo") BetInfo betInfo, Model model,
                                           HttpSession session) {
        return super.bookiePlaceBetBasketball(betInfo, model, session, bookie);
    }

    @RequestMapping(value = "/BillyHill/Football", method = RequestMethod.GET)
    public String bookieGetFootball(Model model) {
        return super.bookieGetFootball(model, bookie);
    }

    @RequestMapping(value = "/BillyHill/Football/{hrefInfo}", method = RequestMethod.GET)
    public String bookieCheckFootballOdds(@PathVariable("hrefInfo") String hrefInfo, Model model, HttpSession session) {
        return super.bookieCheckFootballOdds(hrefInfo, model, session, bookie, oddCalcValue);
    }

    @RequestMapping(value = "/BillyHill/Football/placeBet", method = RequestMethod.POST)
    public String bookiePlaceBetFootball(@ModelAttribute(value = "betinfo") BetInfo betInfo, Model model,
                                         HttpSession session) {
        return super.bookiePlaceBetFootball(betInfo, model, session, bookie);
    }

    @RequestMapping(value = "/BillyHill/bets", method = RequestMethod.GET)
    public String bookieViewBets(Model model, HttpSession session) {
        return super.bookieViewBets(model, session, bookie);
    }
}
