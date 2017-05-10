package laberynth;

import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.simulate.util.SimpleLanguage;
import unalcol.types.collection.vector.Vector;

import java.util.*;

import static java.lang.Integer.parseInt;

public class ArreEater implements AgentProgram {
    private SimpleLanguage sl;
    private Vector<String> cmd = new Vector<>();
    private Percept previousState;
    private Integer x = 0;
    private Integer y = 0;
    private boolean win = false;
    private Integer position = 0;
    private Hashtable<String, ArrayList<String>> hs = new Hashtable<>();
    private Integer newEnergy;
    private Hashtable<ArrayList<Boolean>, Boolean> resources = new Hashtable<>();
    private Stack<ArrayList<Boolean>> lastResource = new Stack<>();
    private Stack<Integer> lastEnergy = new Stack<>();


    public ArreEater(SimpleLanguage sl) {
        this.sl = sl;
        this.previousState = null;
    }

    private void Front_move() {
        if (position == 0) {
            cmd.add(sl.getAction(2));
        } else if (position == 1) {
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(2));
        } else if (position == 2) {
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(2));
        } else {
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(2));
        }
        position = 0;
    }

    private void Back_move() {
        if (position == 2) {
            cmd.add(sl.getAction(2));
        } else if (position == 3) {
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(2));
        } else if (position == 0) {
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(2));
        } else {
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(2));
        }
        position = 2;
    }

    private void Right_move() {
        if (position == 1) {
            cmd.add(sl.getAction(2));
        } else if (position == 2) {
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(2));
        } else if (position == 3) {
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(2));
        } else {
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(2));
        }
        position = 1;
    }

    private void Left_move() {
        if (position == 3) {
            cmd.add(sl.getAction(2));
        } else if (position == 0) {
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(2));
        } else if (position == 1) {
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(2));
        } else {
            cmd.add(sl.getAction(3));
            cmd.add(sl.getAction(2));
        }
        position = 3;
    }

    private void posUpOp(int x, int y) {
        ArrayList arr = new ArrayList();
        arr.add("0");
        arr.add(x + " " + y);
        hs.get(x + " " + y).add(x + " " + (y + 1));
        hs.put(x + " " + (y + 1), arr);
    }

    private void posDownOp(int x, int y) {
        ArrayList arr = new ArrayList();
        arr.add("0");
        arr.add(x + " " + y);
        hs.get(x + " " + y).add(x + " " + (y - 1));
        hs.put(x + " " + (y - 1), arr);
    }

    private void posRightOp(int x, int y) {
        ArrayList arr = new ArrayList();
        arr.add("0");
        arr.add(x + " " + y);
        hs.get(x + " " + y).add((x + 1) + " " + y);
        hs.put((x + 1) + " " + y, arr);
    }

    private void posLeftOp(int x, int y) {
        ArrayList arr = new ArrayList();
        arr.add("0");
        arr.add(x + " " + y);
        hs.get(x + " " + y).add((x - 1) + " " + y);
        hs.put((x - 1) + " " + y, arr);
    }

    @Override
    public Action compute(Percept p) {


        if (this.cmd.size() == 0) {

            boolean PF = ((Boolean) p.getAttribute(sl.getPercept(0)));
            boolean PD = ((Boolean) p.getAttribute(sl.getPercept(1)));
            boolean PA = ((Boolean) p.getAttribute(sl.getPercept(2)));
            boolean PI = ((Boolean) p.getAttribute(sl.getPercept(3)));
            boolean MT = ((Boolean) p.getAttribute(sl.getPercept(4)));
            boolean FAIL = ((Boolean) p.getAttribute(sl.getPercept(5)));
            boolean AF = (Boolean) p.getAttribute(sl.getPercept(6));
            boolean AD = (Boolean) p.getAttribute(sl.getPercept(7));
            boolean AA = (Boolean) p.getAttribute(sl.getPercept(8));
            boolean AI = (Boolean) p.getAttribute(sl.getPercept(9));


            if (!hs.containsKey(x + " " + y)) {
                ArrayList arr = new ArrayList();
                arr.add("1");
                hs.put(x + " " + y, arr);
            }
            if (goalTest(p))
                cmd.add(sl.getAction(1));

            if (MT)
                cmd.add(sl.getAction(1));

            if ((Boolean) p.getAttribute(sl.getPercept(10))) {
                newEnergy = (Integer) p.getAttribute(sl.getPercept(15));
                ArrayList resource = new ArrayList() {{
                    add((Boolean) p.getAttribute(sl.getPercept(11)));
                    add((Boolean) p.getAttribute(sl.getPercept(12)));
                    add((Boolean) p.getAttribute(sl.getPercept(13)));
                    add((Boolean) p.getAttribute(sl.getPercept(14)));
                }};

                if (!lastResource.empty()) {
                    if (resource.equals(lastResource.peek())) {
                        if (newEnergy < lastEnergy.pop()) {
                            if (!resources.containsKey(resource))
                                this.resources.put(lastResource.pop(), false);
                        } else {
                            if (!resources.containsKey(resource))
                                this.resources.put(lastResource.pop(), true);
                            return new Action(sl.getAction(4));
                        }
                    }
                } else {
                    if (newEnergy < 40) {
                        if (resources.containsKey(resource)) {
                            if (resources.get(resource)) {
                                return new Action(sl.getAction(4));
                            }
                        } else {
                            lastEnergy.push(newEnergy);
                            lastResource.push(resource);
                            return new Action(sl.getAction(4));
                        }
                    }
                }
            }


            if (!PF && !AF) {
                if (position == 0 && !hs.containsKey(x + " " + (y + 1))) posUpOp(x, y);
                if (position == 2 && !hs.containsKey(x + " " + (y - 1))) posDownOp(x, y);
                if (position == 1 && !hs.containsKey((x + 1) + " " + y)) posRightOp(x, y);
                if (position == 3 && !hs.containsKey((x - 1) + " " + y)) posLeftOp(x, y);

            }

            if (!PD && !AD) {
                if (position == 3 && !hs.containsKey(x + " " + (y + 1))) posUpOp(x, y);
                if (position == 1 && !hs.containsKey(x + " " + (y - 1))) posDownOp(x, y);
                if (position == 0 && !hs.containsKey((x + 1) + " " + y)) posRightOp(x, y);
                if (position == 2 && !hs.containsKey((x - 1) + " " + y)) posLeftOp(x, y);

            }

            if (!PI && !AI) {
                if (position == 1 && !hs.containsKey(x + " " + (y + 1))) posUpOp(x, y);
                if (position == 3 && !hs.containsKey(x + " " + (y - 1))) posDownOp(x, y);
                if (position == 2 && !hs.containsKey((x + 1) + " " + y)) posRightOp(x, y);
                if (position == 0 && !hs.containsKey((x - 1) + " " + y)) posLeftOp(x, y);
            }

            if (!PA && !AA) {
                if (position == 2 && !hs.containsKey(x + " " + (y + 1))) posUpOp(x, y);
                if (position == 0 && !hs.containsKey(x + " " + (y - 1))) posDownOp(x, y);
                if (position == 3 && !hs.containsKey((x + 1) + " " + y)) posRightOp(x, y);
                if (position == 1 && !hs.containsKey((x - 1) + " " + y)) posLeftOp(x, y);
            }
            //System.out.println(x+" "+y);
            //System.out.println(hs);

            Queue<String> q = new LinkedList();
            Stack<String> S = new Stack<>();
            q.add(x + " " + y);
            LinkedList<String> visitados = new LinkedList();
            String nodo, nodo2;
            boolean b = false;
            while (!q.isEmpty()) {
                nodo = q.poll();
                //System.out.print(nodo);
                visitados.add(nodo);
                for (int i = 1; i < hs.get(nodo).size(); i++) {
                    if (!visitados.contains(hs.get(nodo).get(i))) {
                        nodo2 = hs.get(nodo).get(i);
                        q.add(nodo2);
                        if (hs.get(nodo2).get(0) == "0") {
                            //System.out.println(nodo2);
                            S.add(nodo2);
                            b = true;
                            break;
                        }
                    }
                }
                if (b) {
                    break;
                }
            }
            while (!visitados.isEmpty()) {
                nodo = visitados.pollLast();
                if (hs.get(nodo).contains(S.peek())) {
                    S.push(nodo);
                }
            }
            S.pop();
            while (!S.isEmpty()) {
                //System.out.println(S);
                String[] s = S.pop().split(" ");
                if (parseInt(s[0]) > x) {
                    Right_move();
                    x = x + 1;
                } else if (parseInt(s[0]) < x) {
                    Left_move();
                    x = x - 1;
                } else if (parseInt(s[1]) > y) {
                    Front_move();
                    y = y + 1;
                } else if (parseInt(s[1]) < y) {
                    Back_move();
                    y = y - 1;
                }
            }

            //System.out.println(x+" "+y);
            hs.get(x + " " + y).remove(0);
            hs.get(x + " " + y).add(0, "1");
            //          System.out.print(x+" "+y);


        }
        String action = cmd.get(0);
        cmd.remove(0);
        return new Action(action);

    }

    public Boolean goalTest(Percept p) {
        return (Boolean) p.getAttribute(this.sl.getPercept(4));
    }

    @Override
    public void init() {
        this.cmd.clear();
    }

}