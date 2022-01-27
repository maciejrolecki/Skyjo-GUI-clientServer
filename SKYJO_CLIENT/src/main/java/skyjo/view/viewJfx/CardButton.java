package skyjo.view.viewJfx;

import javafx.scene.control.Button;
import skyjo.model.*;

/**
 * The type Card button.
 */
public class CardButton extends Button {
    private Card card;

    /**
     * Instantiates a new Card button.
     *
     * @param card   the card
     */
    public CardButton(Card card) {
        this.card = card;
    }

    /**
     * Gets card.
     *
     * @return the card
     */
    public Card getCard() {
        return card;
    }

    /**
     * Sets card.
     *
     * @param card the card
     */
    public void setCard(Card card) {
        this.card = card;
    }

}
