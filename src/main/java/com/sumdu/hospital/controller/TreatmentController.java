package com.sumdu.hospital.controller;

import com.sumdu.hospital.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class TreatmentController {
    private ApplicationContext context;
    private Card card;

    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
