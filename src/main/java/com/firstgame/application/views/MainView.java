package com.firstgame.application.views;

import com.firstgame.application.views.chat.ChatView;
import com.firstgame.application.views.tictactoe.TicTacToeBoardView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("My View")
@Route("")
public class MainView extends Composite<VerticalLayout> {

    TicTacToeBoardView board;
    ChatView chat;

    public MainView() {
        HorizontalLayout layoutRow = new HorizontalLayout();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        VerticalLayout layoutColumn3 = new VerticalLayout();
        board = new TicTacToeBoardView();
        chat = new ChatView();

        getContent().setWidth("100%");
//        addClassName("main-view");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.addClassName(Gap.MEDIUM);
//        layoutRow.addClassName("....");
        layoutRow.setWidth("100%");
        layoutRow.setHeight("100vh");
        layoutRow.getStyle().set("flex-grow", "1");

        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutColumn2.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutColumn2.setAlignItems(Alignment.CENTER);

        layoutColumn3.setWidth("596px");
        layoutColumn3.setHeight("95%");
        layoutColumn3.getStyle().set("flex-grow", "1");
        layoutColumn3.setJustifyContentMode(JustifyContentMode.END);
        layoutColumn3.setAlignItems(Alignment.END);
        layoutColumn3.getStyle().set("border-radius", "5px");
        layoutColumn3.getElement().getStyle().set("background-color", "#f5f5f5");
        layoutColumn3.getElement().getStyle().set("color", "black");

        getContent().add(layoutRow);
        layoutRow.add(layoutColumn2);
        layoutRow.add(layoutColumn3);

        layoutColumn2.add(board);
        layoutColumn3.add(chat);

    }
}
