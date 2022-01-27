package skyjo.view.buttonHandlers;

import skyjo.controller.Controller;
import skyjo.model.*;
import skyjo.view.viewJfx.SkyjoView;

/**
 * The main class that group all the handlers for each buttons
 *
 * @author
 */

public class ButtonHandlers {

    private final HandlerDiscardB discardB;
    private final HandlerCardB handlerCardB;
    private final HandlerPickB handlerPickB;
    private final DropButtonH dropButtonH;
    private final KeepButtonH keepButtonH;
    private final ChoiceBoxHandler choiceBoxHandler;

    /**
     * Button Handler constructor
     *
     * @param controller use to create the handlers
     * @param game
     * @param view
     */
    public ButtonHandlers(Controller controller, Facade game, SkyjoView view) {
        discardB = new HandlerDiscardB(view, controller);
        handlerCardB = new HandlerCardB(view, controller);
        handlerPickB = new HandlerPickB(view, controller);
        dropButtonH = new DropButtonH(view, controller, game);
        keepButtonH = new KeepButtonH(view, controller);
        choiceBoxHandler = new ChoiceBoxHandler(view, controller);

    }

    /**
     * defined the handler of all the button use in the view
     */
    public void definedHandlers() {
        discardB.definedActionHandler();
        handlerCardB.addEventToCards();
        handlerPickB.definedActionHandler();
        dropButtonH.addEvent();
        keepButtonH.addEvent();
        choiceBoxHandler.definedEventAction();
    }

}
