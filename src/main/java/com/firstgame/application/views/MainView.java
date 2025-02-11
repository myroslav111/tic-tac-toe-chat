package com.firstgame.application.views;

import com.firstgame.application.views.chat.ChatComponent;
import com.firstgame.application.views.tictactoe.TicTacToeBoardComponent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("My View")
@Route("")
public class MainView extends Composite<VerticalLayout> {

    TicTacToeBoardComponent board;
    ChatComponent chat;

    public MainView() {
        HorizontalLayout layoutRow = new HorizontalLayout();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        VerticalLayout layoutColumn3 = new VerticalLayout();
        board = new TicTacToeBoardComponent();
        chat = new ChatComponent();

        addClassName("main-view");

        layoutRow.addClassName("layout-row");

        layoutColumn2.addClassName("layout-column-2");

        layoutColumn3.addClassName("layout-column-3");

        getContent().add(layoutRow);
        layoutRow.add(layoutColumn2);
        layoutRow.add(layoutColumn3);

        layoutColumn2.add(board);
        layoutColumn3.add(chat);

    }
}
