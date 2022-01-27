package skyjo.view.buttonHandlers;

import skyjo.view.viewJfx.SkyjoView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import skyjo.controller.Controller;

/**
 * The Handler for pick button.
 */
public class HandlerPickB implements EventHandler<MouseEvent> {

    private final SkyjoView view;
    private final Controller controller;

    public HandlerPickB(SkyjoView view, Controller controller) {
        this.view = view;
        this.controller = controller;

    }

    /**
     * Handle method for pick button
     *
     * @param event mouse click even
     */
    @Override
    public void handle(MouseEvent event) {
        view.setLabelAdviceMessage(2, this.controller.getClient().getUser());
        view.consumePickButton();
        view.consumeDiscardButton();
        view.setClickPick(true);
        this.controller.sendClickPitchedAskForCard();

    }

    /**
     * Add action handler.
     */
    public void definedActionHandler() {
        view.pickHandButton().addEventHandler(MouseEvent.MOUSE_CLICKED, this);
    }
}
