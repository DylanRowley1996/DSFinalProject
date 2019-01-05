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
public class Bet123Controller extends GeneralBookieController {

    private String bookie = "Bet123";
    private double oddCalcValue = 0.890143;

    @RequestMapping(value = "/Bet123", method = RequestMethod.GET)
    public String checkBookie(Model model, HttpSession session) {
        session.setAttribute("bookie", bookie);
        return super.checkBookie(model,session,bookie);
    }

    @RequestMapping(value = "/bet-in-Bet123/Basketball", method = RequestMethod.GET)
    public String bookieGetBasketball(Model model) {
        return super.bookieGetBasketball(model, bookie);
    }

    @RequestMapping(value = "/bet-in-Bet123/Basketball/{hrefInfo}", method = RequestMethod.GET)
    public String bookieCheckBasketballOdds(@PathVariable("hrefInfo") String hrefInfo, Model model, HttpSession session) {
        return super.bookieCheckBasketballOdds(hrefInfo, model, session,bookie, oddCalcValue);
    }

    @RequestMapping(value = "/bet-in-Bet123/Basketball/placeBet", method = RequestMethod.POST)
    public String bookiePlaceBetBasketball(@ModelAttribute(value = "betinfo") BetInfo betInfo, Model model,
                                           HttpSession session) {
        return super.bookiePlaceBetBasketball(betInfo, model, session, bookie);
    }

    @RequestMapping(value = "/bet-in-Bet123/Football", method = RequestMethod.GET)
    public String bookieGetFootball(Model model) {
        return super.bookieGetFootball(model, bookie);
    }

    @RequestMapping(value = "/bet-in-bookie/Football/{hrefInfo}", method = RequestMethod.GET)
    public String bookieCheckFootballOdds(@PathVariable("hrefInfo") String hrefInfo, Model model, HttpSession session) {
        return super.bookieCheckFootballOdds(hrefInfo, model, session, bookie, oddCalcValue);
    }

    @RequestMapping(value = "/bet-in-bookie/Football/placeBet", method = RequestMethod.POST)
    public String bookiePlaceBetFootball(@ModelAttribute(value = "betinfo") BetInfo betInfo, Model model,
                                         HttpSession session) {
        return super.bookiePlaceBetFootball(betInfo, model, session, bookie);
    }
}
