package com.sumdu.hospital.controller;

import com.sumdu.hospital.model.Card;
import org.springframework.stereotype.Controller;

@Controller
public class MedicalCardController {
    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    private Card card;
}
