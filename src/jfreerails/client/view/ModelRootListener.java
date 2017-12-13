package jfreerails.client.view;

public interface ModelRootListener {
    /**
     * called when the modelRoot has changed significantly (eg a new world
     * model has been loaded)
     */
    public void modelRootChanged();
}