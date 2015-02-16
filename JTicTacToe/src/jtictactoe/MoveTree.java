/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jtictactoe;

import java.util.*;

/**
 *
 * @author revant
 */
public class MoveTree {
    private static final int MAX_DEPTH = 4;
    
    ArrayList<MoveTree> Child = new ArrayList<>();
    MoveTree NextMove;
    int stateX, stateY;
    int score;
    int LeafScore;
    String state;
    String[][] map;
    int Depth;
    int mapSize;
    boolean Relevant;
    boolean isCompleate;
    boolean player;
    boolean computer;
    boolean calculated;
    public MoveTree(int depth, int size, int x, int y, String[][] map){
        this.Depth = depth;
        this.stateX = x;
        this.stateY = y;      
        this.score = 0;
        this.Relevant = true;
        this.isCompleate = false;
        this.calculated = false;
        this.mapSize = size;
        this.map = new String[size][size];
        for(int i = 0; i < size; i++){
            System.arraycopy(map[i], 0, this.map[i], 0, size);
        }
    }
    
    public MoveTree(int size){
        this.Depth = 0;
        this.score = 0;
        this.Relevant = true;
        this.isCompleate = false;
        this.mapSize = size;
        map = new String[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                map[i][j] = ".";
            }
        }
        this.state = ".";
        this.calculated = false;
    }
    
    public void setState(String state){
        this.state = state;
        this.map[this.stateX][this.stateY] = this.state;
        this.Compleated();
    }
    
    public boolean isPlayer(){
        return "X".equals(this.state);
    }            
    
    private void calculateScore(){
        if(this.calculated) return;
        int player1, computer1;
        int player2, computer2;
        this.score = 0;
        
        for(int i = 0; i < this.mapSize; i++){
            player2 = computer2 = 0;
            player1 = computer1 = 0;
            
            for(int j = 0; j < this.mapSize; j++){
                switch (map[i][j]) {
                    case "X":
                        player1++;
                        break;
                    case "O":
                        computer1++;
                        break;
                }
                
                switch(map[j][i]){
                    case "X":
                        player2++;
                        break;
                    case "O":
                        computer2++;
                        break;
                }
            }
            
            if(computer1 == 0 && player1 != 0)this.score -= Math.pow(10, player1);
            else if(player1 == 0 && computer1 != 0)this.score += Math.pow(10, computer1);
            if(computer2 == 0 && player2 != 0)this.score -= Math.pow(10, player2);
            else if(player2 == 0 && computer2 != 0)this.score += Math.pow(10, computer2);
        }                
        
        player1 = player2 = computer1 = computer2 = 0;
        for(int i = 0; i < this.mapSize; i++){
            switch(map[i][i]){
                case "X":
                    player1++;
                    break;
                case "O":
                    computer1++;
                    break;
            }
            switch(map[this.mapSize - 1 - i][i]){
                 case "X":
                    player2++;
                    break;
                case "O":
                    computer2++;
                    break;
            }
        }
        
        if(computer1 == 0 && player1 != 0)this.score -= Math.pow(10, player1);
        else if(player1 == 0 && computer1 != 0)this.score += Math.pow(10, computer1);
        if(computer2 == 0 && player2 != 0)this.score -= Math.pow(10, player2);
        else if(player2 == 0 && computer2 != 0)this.score += Math.pow(10, computer2);
        this.calculated = true;
    }
    
    public int getScore(){
        return this.score;
    }
    
    public void generateChild(){
        if(!this.isCompleate){
            for(int i = 0; i < this.mapSize; i++){
                for(int j = 0; j < this.mapSize; j++){
                    switch(map[i][j]){
                        case "X" :
                        case "O" :
                            break;
                        default:
                            MoveTree newTree = new MoveTree(this.Depth + 1, this.mapSize, i, j, map);
                            if(this.isPlayer())newTree.setState("O");
                            else newTree.setState("X");
                            this.Child.add(newTree);
//                            newTree.printMap();
                            break;
                    }
                }
            }
        }
    }
    
    private boolean Compleated(){
        int used = 0;
        int X1, X2, O1, O2;
        player = false;
        computer = false;
        for(int i = 0; i < this.mapSize; i++){
            X1 = X2 = O1 = O2 = 0;
            
            for(int j = 0; j < this.mapSize; j++){
                switch(map[i][j]){
                    case "X" :
                        used++;
                        X1++;
                        break;
                    case "O" :
                        used++;
                        O1++;
                        break;
                }
                
                switch(map[j][i]){
                    case "X" :
                        X2++;
                        break;
                    case "O" :
                        O2++;
                        break;
                }
            }
            
            player = (X1 == this.mapSize) || (X2 == this.mapSize);
            computer = (O1 == this.mapSize) || (O2 == this.mapSize);
            if(computer || player) break;
        }

        if(!player && !computer){
            X1 = O1 = 0;
            X2 = O2 = 0;
            for(int i = 0; i < this.mapSize; i++){
                switch(map[i][i]){
                    case "X" :
                        X1++;
                        break;
                    case "O" :
                        O1++;
                        break;
                }
                
                switch(map[this.mapSize - 1 - i][i]){
                    case "X" :
                        X2++;
                        break;
                    case "O" :
                        O2++;
                        break;
                }
            }
            
            player = (X1 == this.mapSize) || (X2 == this.mapSize);
            computer = (O1 == this.mapSize) || (O2 == this.mapSize);
        }

        this.isCompleate = (used == Math.pow(this.mapSize, 2)) || player || computer;
        return this.isCompleate;
    }
    
    public void MinMax(){ 
        ArrayList<Integer> Leafs = new ArrayList<>();
        ArrayList<MoveTree> Moves = new ArrayList<>();
        if(this.Child.isEmpty()) this.generateChild();
        
        Child.stream().filter((now) -> (now.isRelevant())).forEach((now) ->{
            Leafs.add(now.MinMax(0));
            Moves.add(now);
        });
        int mv = Moves.size();
        int values = Leafs.get(0);
        this.NextMove = Moves.get(0);
        for(int i = 0; i < mv; i++){
            if(Leafs.get(i) > values){
                values = Leafs.get(i);
                this.NextMove = Moves.get(i);
            }
        }
        this.score = values;
    }
    
    private int MinMax(int Base){
        if((Base >= MAX_DEPTH) || isCompleate){
            this.calculateScore();
        }
        else{
            ArrayList<Integer> Leafs = new ArrayList<>();
            if(this.Child.isEmpty()) this.generateChild();
            Child.stream().filter((now) -> (now.isRelevant())).forEach((now) -> {
                Leafs.add(now.MinMax(Base + 1));                
            });
            if(this.isPlayer()) this.score = Collections.max(Leafs);
            else this.score = Collections.min(Leafs);
        }
        return this.score;
    } 
    
    private void alphaBetaPruning(int base, int alpha, int beta){
        if(base < MAX_DEPTH){
            if(base > 0){
                if(!(this.score <= alpha && this.score >= beta)) {
                    this.Relevant = false;
                    System.out.printf("Prunned node with score %d on depth %d\n", this.score, this.Depth);
                }
            }
            if(this.Relevant)
            this.Child.stream().filter((now) -> (now.isRelevant())).forEach((now) -> {
                    if(this.isPlayer()) now.alphaBetaPruning(base + 1, this.score, beta);
                    else now.alphaBetaPruning(base + 1, alpha, this.score);
            });
        }
    }
    
    public void alphaBetaPruning(){
        this.Child.stream().forEach((now) -> {
            if(now.isRelevant()){
                now.alphaBetaPruning(0, this.score, 0);
            }
        });
    }
    
    public MoveTree toChild(int x, int y){
        MoveTree theOne = null;
        for(MoveTree now : Child){
            if(now.isTheOne(x, y)) theOne = now;
        }
        return  theOne;
    }
    
    private boolean isTheOne(int x, int y){
        return this.stateX == x && this.stateY == y;
    }
    
    public MoveTree nextMove(){
        return this.NextMove;
    }
    
    public int getDepth(){
        return this.Depth;
    }
    
    public boolean isRelevant(){
        return this.Relevant;
    }
    
    public String[][] getMap(){
        return this.map;
    }
    
    public boolean hasCompleated(){
        return this.isCompleate;
    }
    
    public void printMap(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }
    
    public void printWinner(){
        if(player){
            System.out.println("The player has won!");
        }
        else if(computer){
            System.out.println("The computer has won!");
        }
        else{
            System.out.println("It's a tie!");
        }
    }
    
    public int[] getState(){
        int[] vaue = {stateX, stateY};
        return vaue;
    }
}
