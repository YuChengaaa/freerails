package jfreerails.client.top;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import jfreerails.client.common.ActionAdapter;
import jfreerails.client.renderer.MapRenderer;
import jfreerails.client.renderer.ViewLists;
import jfreerails.client.renderer.ZoomedOutMapRenderer;
import jfreerails.client.view.CashJLabel;
import jfreerails.client.view.DateJLabel;
import jfreerails.client.view.DetailMapView;
import jfreerails.client.view.DialogueBoxController;
import jfreerails.client.view.MainMapAndOverviewMapMediator;
import jfreerails.client.view.MapCursor;
import jfreerails.client.view.MapViewJComponentConcrete;
import jfreerails.client.view.MapViewMoveReceiver;
import jfreerails.client.view.ModelRoot;
import jfreerails.client.view.ModelRootListener;
import jfreerails.client.view.OverviewMapJComponent;
import jfreerails.client.view.ServerControlModel;
import jfreerails.client.view.StationPlacementCursor;
import jfreerails.client.view.TrainsJTabPane;
import jfreerails.controller.MoveChainFork;
import jfreerails.controller.MoveReceiver;
import jfreerails.controller.UntriedMoveReceiver;
import jfreerails.world.top.ReadOnlyWorld;
import jfreerails.move.ChangeGameSpeedMove;
import jfreerails.move.Move;
import jfreerails.world.common.GameSpeed;
import jfreerails.world.top.ITEM;


//PAUSE Check Box   import javax.swing.JCheckBoxMenuItem;
public class GUIComponentFactoryImpl implements GUIComponentFactory,
    ModelRootListener {
    private ModelRoot modelRoot;
    private ServerControlModel sc;
    private DateJLabel datejLabel;
    private CashJLabel cashjLabel;

    /**
     * This is the panel at the bottom right of the screen
     */
    private TrainsJTabPane trainsJTabPane;
    private javax.swing.JMenu helpMenu;
    private final DialogueBoxController dialogueBoxController;
    private ViewLists viewLists;
    private ReadOnlyWorld world;
    UserInputOnMapController userInputOnMapController;
    StationTypesPopup stationTypesPopup;
    BuildMenu buildMenu;
    JMenu displayMenu;
    JPanel overviewMapContainer;
    MapViewJComponentConcrete mapViewJComponent;
    private JScrollPane mainMapScrollPane1;
    MapRenderer overviewMap;
    DetailMapView mainMap;
    Rectangle r = new Rectangle(10, 10, 10, 10);
    ClientJFrame clientJFrame;
    UserMessageGenerator userMessageGenerator;
    ActionAdapter speedActions;

    public GUIComponentFactoryImpl(ModelRoot mr) {
        modelRoot = mr;
        userInputOnMapController = new UserInputOnMapController(modelRoot);
        buildMenu = new jfreerails.client.top.BuildMenu();
        mapViewJComponent = new MapViewJComponentConcrete();
        mainMapScrollPane1 = new JScrollPane();
        overviewMapContainer = new OverviewMapJComponent(r);
        stationTypesPopup = new StationTypesPopup();

        MainMapAndOverviewMapMediator mediator = new MainMapAndOverviewMapMediator();
        mediator.setup(overviewMapContainer, mainMapScrollPane1.getViewport(),
            mapViewJComponent, r);

        trainsJTabPane = new TrainsJTabPane();
        datejLabel = new DateJLabel();

        cashjLabel = new CashJLabel();

        clientJFrame = new ClientJFrame(this);
        dialogueBoxController = new DialogueBoxController(clientJFrame,
                modelRoot);
    }

    private void setup(ViewLists vl, ReadOnlyWorld w) {
        viewLists = vl;
        world = w;

        UntriedMoveReceiver receiver = modelRoot.getReceiver();

        if (!vl.validate(world)) {
            throw new IllegalArgumentException("The specified" +
                " ViewLists are not comaptible with the clients" + "world!");
        }

        //create the main and overview maps
        mainMap = new DetailMapView(world, viewLists);

        Dimension maxSize = new Dimension(200, 200);
        overviewMap = ZoomedOutMapRenderer.getInstance(world, maxSize);

        //init the move handlers
        MoveReceiver overviewmapMoveReceiver = new MapViewMoveReceiver(mainMap);

        MoveChainFork moveFork = modelRoot.getMoveChainFork();
        moveFork.addSplitMoveReceiver(overviewmapMoveReceiver);

        MoveReceiver mainmapMoveReceiver = new MapViewMoveReceiver(overviewMap);
        moveFork.addSplitMoveReceiver(mainmapMoveReceiver);

        //Never read!
        //StationBuilder sb = new StationBuilder(receiver, w,
        //        modelRoot.getPlayerPrincipal());
        stationTypesPopup.setup(modelRoot, mainMap.getStationRadius());

        mapViewJComponent.setup(mainMap, w);
        modelRoot.setCursor(mapViewJComponent.getMapCursor());

        //setup the the main and overview map JComponents
        dialogueBoxController.setDefaultFocusOwner(mapViewJComponent);

        userInputOnMapController.setup(mapViewJComponent,
            modelRoot.getTrackMoveProducer(), stationTypesPopup,
            this.modelRoot, dialogueBoxController, receiver);

        buildMenu.setup(world, modelRoot);
        mainMapScrollPane1.setViewportView(this.mapViewJComponent);

        ((OverviewMapJComponent)overviewMapContainer).setup(overviewMap);

        datejLabel.setup(modelRoot, null);
        cashjLabel.setup(modelRoot, null);
        trainsJTabPane.setup(world, vl, modelRoot);

        MapCursor mapCursor = modelRoot.getCursor();
        mapCursor.addCursorEventListener(trainsJTabPane);
        trainsJTabPane.setMapCursor(mapCursor);
        dialogueBoxController.setup(world, vl, modelRoot.getMoveChainFork(),
            modelRoot.getReceiver(), mapCursor);

        StationPlacementCursor stationPlacementCursor = new StationPlacementCursor(modelRoot,
                mainMap.getStationRadius(), mapViewJComponent);
        modelRoot.setUserMessageLogger(this.mapViewJComponent);

        userMessageGenerator = new UserMessageGenerator(this.modelRoot);
        moveFork.add(userMessageGenerator);

        int gameSpeed = ((GameSpeed)world.get(ITEM.GAME_SPEED)).getSpeed();

        // selecting action radio button
        for (Enumeration enum = speedActions.getActions();
                enum.hasMoreElements();) {
            Action action = (Action)enum.nextElement();

            if (action.equals(new Integer(gameSpeed))) {
                String actionName = (String)action.getValue(Action.NAME);
                speedActions.setSelectedItem(actionName);
            }
        }

        /**
         *  @todo FIX ME   -- is there better possibility to pritne the same text as in UserMessageGenerator.processMove ?
         *                    maybe: to add logSpeed(int) into MessageLogger ?
         */
        if (gameSpeed <= 0) {
            modelRoot.getUserMessageLogger().showMessage("Game is paused.");
        } else {
            modelRoot.getUserMessageLogger().hideMessage();

            String gameSpeedDesc = modelRoot.getServerControls()
                                            .getGameSpeedDesc(gameSpeed);
            modelRoot.getUserMessageLogger().println("Game speed: " +
                gameSpeedDesc);
        }

        moveFork.addSplitMoveReceiver(new MoveReceiver() {
                public void processMove(Move move) {
                    if (move instanceof ChangeGameSpeedMove) {
                        ChangeGameSpeedMove speedMove = (ChangeGameSpeedMove)move;

                        for (Enumeration enum = speedActions.getActions();
                                enum.hasMoreElements();) {
                            Action action = (Action)enum.nextElement();
                            String actionName = (String)action.getValue(Action.NAME);

                            if (actionName.equals(modelRoot.getServerControls()
                                                               .getGameSpeedDesc(speedMove.getNewSpeed()))) {
                                speedActions.setSelectedItem(actionName);
                            }

                            break;
                        }
                    }
                }
            });
    }

    public JPanel createOverviewMap() {
        return overviewMapContainer;
    }

    public JScrollPane createMainMap() {
        return mainMapScrollPane1;
    }

    public JMenu createBuildMenu() {
        return buildMenu;
    }

    public JMenu createDisplayMenu() {
        displayMenu = new JMenu("Display");
        displayMenu.setMnemonic(68);

        JMenuItem trainOrdersJMenuItem = new JMenuItem("Train Orders");
        trainOrdersJMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialogueBoxController.showTrainOrders();
                }
            });

        JMenuItem stationInfoJMenuItem = new JMenuItem("Station Info");
        stationInfoJMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialogueBoxController.showStationInfo(0);
                }
            });

        JMenuItem trainListJMenuItem = new JMenuItem("Train List");
        trainListJMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialogueBoxController.showTrainList();
                }
            });

        displayMenu.add(trainOrdersJMenuItem);
        displayMenu.add(stationInfoJMenuItem);
        displayMenu.add(trainListJMenuItem);

        return displayMenu;
    }

    public JMenu createGameMenu() {
        sc = modelRoot.getServerControls();

        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(71);

        JMenuItem quitJMenuItem = new JMenuItem("Exit Game");
        quitJMenuItem.setMnemonic(88);

        quitJMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

        final JMenu newGameJMenu = new JMenu(sc.getNewGameAction());
        newGameJMenu.addMenuListener(new MenuListener() {
                public void menuSelected(MenuEvent e) {
                    newGameJMenu.removeAll();

                    Enumeration actions = sc.getMapNames().getActions();

                    while (actions.hasMoreElements()) {
                        JMenuItem mi = new JMenuItem((Action)actions.nextElement());
                        newGameJMenu.add(mi);
                    }
                }

                public void menuCanceled(MenuEvent e) {
                }

                public void menuDeselected(MenuEvent e) {
                }
            });

        JMenuItem saveGameJMenuItem = new JMenuItem(sc.getSaveGameAction());

        JMenuItem loadGameJMenuItem = new JMenuItem(sc.getLoadGameAction());

        JMenuItem newspaperJMenuItem = new JMenuItem("Newspaper");
        newspaperJMenuItem.setMnemonic(78);

        newspaperJMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialogueBoxController.showNewspaper("Headline");
                    //glassPanel.setVisible(true);
                }
            });

        JMenuItem incomeStatementJMenuItem = new JMenuItem("Income Statement");
        incomeStatementJMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialogueBoxController.showIncomeStatement();
                }
            });

        JMenuItem balanceSheetJMenuItem = new JMenuItem("Balance Sheet");
        balanceSheetJMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialogueBoxController.showBalanceSheet();
                }
            });

        //Set up the gamespeed submenu.
        JMenu gameSpeedSubMenu = new JMenu("Game Speed");

        /* PAUSE CheckBox
                final JCheckBoxMenuItem speedMI = new JCheckBoxMenuItem(sc.getPauseAction());
        //        mi.setModel((ButtonModel)buttonModels.nextElement());
                gameSpeedSubMenu.add(speedMI);
        */
        ButtonGroup group = new ButtonGroup();

        speedActions = sc.getSetTargetTickPerSecondActions();

        Enumeration buttonModels = speedActions.getButtonModels();
        Enumeration actions = speedActions.getActions();

        while (buttonModels.hasMoreElements()) {
            JRadioButtonMenuItem mi = new JRadioButtonMenuItem((Action)actions.nextElement());
            mi.setModel((ButtonModel)buttonModels.nextElement());

            /* PAUSE CheckBox
                        mi.addActionListener(new ActionListener() {
                          public void actionPerformed(ActionEvent e) {
                            if (speedMI.isSelected()) {
                              // paused => unchecking the pause checkBox
                              speedMI.setSelected(false);
                            }
                          }
                        });
            */
            group.add(mi);
            gameSpeedSubMenu.add(mi);
        }

        gameMenu.add(newGameJMenu);
        gameMenu.addSeparator();
        gameMenu.add(loadGameJMenuItem);
        gameMenu.add(saveGameJMenuItem);
        gameMenu.addSeparator();
        gameMenu.add(balanceSheetJMenuItem);
        gameMenu.add(incomeStatementJMenuItem);
        gameMenu.add(gameSpeedSubMenu);
        gameMenu.add(newspaperJMenuItem);
        gameMenu.addSeparator();
        gameMenu.add(quitJMenuItem);

        return gameMenu;
    }

    ViewLists getViewLists() {
        return modelRoot.getViewLists();
    }

    public JFrame createClientJFrame(String title) {
        clientJFrame.setTitle(title);

        return clientJFrame;
    }

    public JMenu createHelpMenu() {
        helpMenu = new javax.swing.JMenu("Help");

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    dialogueBoxController.showAbout();
                }
            });

        JMenuItem how2play = new JMenuItem("Getting started");
        how2play.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    dialogueBoxController.showHow2Play();
                }
            });

        JMenuItem showControls = new JMenuItem("Show game controls");
        showControls.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    dialogueBoxController.showGameControls();
                }
            });

        JMenuItem showJavaProperties = new JMenuItem("Show Java Properties");
        showJavaProperties.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    dialogueBoxController.showJavaProperties();
                }
            });

        helpMenu.add(showControls);
        helpMenu.add(how2play);
        helpMenu.add(showJavaProperties);
        helpMenu.add(about);

        return helpMenu;
    }

    public JTabbedPane createTrainsJTabPane() {
        return trainsJTabPane;
    }

    public JLabel createCashJLabel() {
        return cashjLabel;
    }

    public JLabel createDateJLabel() {
        return datejLabel;
    }

    private void worldModelChanged() {
        /*
         * XXX this is temporary - we should have a formal object to store
         * the clients copy of the model, connections to the server, etc.
         */
        ReadOnlyWorld world = this.modelRoot.getWorld();
        ViewLists viewLists = getViewLists();

        if (!viewLists.validate(world)) {
            throw new IllegalArgumentException();
        }

        setup(viewLists, world);
    }

    public void modelRootChanged() {
        worldModelChanged();
    }
}