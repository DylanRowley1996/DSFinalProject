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
public class PatrickPowerController extends GeneralBookieController {
    private String bookie = "PatrickPower";

    @RequestMapping(value = "/PatrickPower", method = RequestMethod.GET)
    public String checkBookie(Model model) {
        model.addAttribute("bookie", bookie);
        return super.checkBookie(model);
    }

    @RequestMapping(value = "/bet-in-PatrickPower/Basketball", method = RequestMethod.GET)
    public String bookieGetBasketball(Model model) {
        model.addAttribute("bookie", bookie);
        return super.bookieGetBasketball(model);
    }

    @RequestMapping(value = "/bet-in-PatrickPower/Basketball/{hrefInfo}", method = RequestMethod.GET)
    public String bookieCheckBasketballOdds(@PathVariable("hrefInfo") String hrefInfo, Model model, HttpSession session) {
        model.addAttribute("bookie", bookie);
        return super.bookieCheckBasketballOdds(hrefInfo, model, session);
    }

    @RequestMapping(value = "/bet-in-PatrickPower/Basketball/placeBet", method = RequestMethod.POST)
    public String bookiePlaceBetBasketball(@ModelAttribute(value = "betinfo") BetInfo betInfo, Model model,
                                           HttpSession session) {
        model.addAttribute("bookie", bookie);
        return super.bookiePlaceBetBasketball(betInfo, model, session);
    }

    @RequestMapping(value = "/bet-in-PatrickPower/Football", method = RequestMethod.GET)
    public String bookieGetFootball(Model model) {
        model.addAttribute("bookie", bookie);
        return super.bookieGetFootball(model);
    }

    @RequestMapping(value = "/bet-in-PatrickPower/Football/{hrefInfo}", method = RequestMethod.GET)
    public String bookieCheckFootballOdds(@PathVariable("hrefInfo") String hrefInfo, Model model, HttpSession session) {
        model.addAttribute("bookie", bookie);
        return super.bookieCheckFootballOdds(hrefInfo, model, session);
    }

    @RequestMapping(value = "/bet-in-PatrickPower/Football/placeBet", method = RequestMethod.POST)
    public String bookiePlaceBetFootball(@ModelAttribute(value = "betinfo") BetInfo betInfo, Model model,
                                         HttpSession session) {
        model.addAttribute("bookie", bookie);
        return super.bookiePlaceBetFootball(betInfo, model, session);
    }
}
