package ui.assignments.a3battleship

//import ui.assignments.a3battleship.model.Game
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import kotlin.random.Random
import kotlin.system.exitProcess


class Coordinate(myX: Int, myY: Int, myName: String) {
    var x: Int = myX
    var y: Int = myY
    var state = "water"
    var shipName = myName
}
fun myFloor(num: Double): Int{
    if(num % 1 == 0.0){
        return num.toInt()
    }
    if(num > 0){
        return num.toInt()
    }
    return (num - 1).toInt()
}/*
fun toCoord(num: Double): Int{
    return ((num - 50.0)/27.5).toInt()
}*/

fun min(x: Double, y: Double): Double{
    if(x < y){
        return x
    }
    return y
}
class Ship{
    private var name = ""
    var size = 0
    var initialX = 0.0
    var initialY = 0.0
    var x = 875.0/2
    var y = 375.0/2
    var rotated = true
    var selected = true
    var myWidth = 27.5
    var myHeight = 27.5
    private var pos = mutableListOf<Coordinate>()
    fun myRotate(size: Int){
        if(selected) {
            rotated = !rotated
            if (rotated) {
                myWidth = 27.5 * size - 10
                myHeight = 27.5 - 10
                //return Rectangle(x, y, 27.5* size, 27.5 )
            } else {
                myWidth = 27.5 - 10
                myHeight = 27.5 * size - 10
                //return Rectangle(x, y, 27.5, 27.5 * size)
            }
        }
    }
    fun myShip(size: Int, thisX: Double, thisY: Double, myName: String){
        name = myName
        x = thisX
        y = thisY
        this.size = size
        initialX = thisX
        initialY = thisY
        myRotate(size)
        selected = false
    }
    private fun isHit(x: Double, y: Double): Boolean{
        return x >= this.x && x <= this.x + myWidth &&
                y>= this.y && y <= this.y + myHeight

    }



    fun placeShip(thisX: Double, thisY: Double, notSelected: Boolean, ships: MutableList<Ship>){
        val itX = thisX - myWidth/2
        val itY = thisY - myHeight / 2
        if(isHit(thisX, thisY) && notSelected) {
            selected = true
            //println("HELLO WORLD")
            validMove = false
        }
        else if(selected){

            validMove = false
            //var temporaryX = 0.0
            //var temporaryY = 0.0
            /*if(x + myWidth * 27.5>= 311.25){
                x = initialX
                y = initialY
                selected = false
                if(rotated){
                    myRotate(size)
                }
                return
            }*/
            for(i in 0 until 10) {
                for(j in 0 until 10) {
                    val xTrue = itX <= 50.0 + 27.5 * i + 27.5/2 && itX > 50.0 + 27.5 * i - 27.5/2 && itX <= 325 - myWidth + 27.5/2
                    val yTrue = itY <= 50.0 + 27.5 * j + 27.5/2 && itY > 50.0 + 27.5 * j - 27.5/2 && itY <= 325 - myHeight + 27.5/2
                    if (xTrue && yTrue ) {
                        x = 50.0 + 27.5 * i
                        y = 50.0 + 27.5 * j
                        validMove = true
                    }

                    if(validMove) {
                        x += 5
                        y += 5
                        for (l in 0 until ships.size) {
                            if (ships[l].name != name) {
                                for (k in 0 until size) {
                                    var containsCoord = false
                                    if (rotated) {
                                        for (n in 0 until ships[l].pos.size) {
                                            if (ships[l].pos[n].x == i + k && ships[l].pos[n].y == j) {
                                                println(i + k)
                                                println(j)
                                                containsCoord = true
                                                validMove = false
                                                break
                                            }

                                        }
                                        //if(containsCoord){break}
                                    } else {
                                        for (n in 0 until ships[l].pos.size) {
                                            if (ships[l].pos[n].x == i && ships[l].pos[n].y == j + k) {
                                                println(i)
                                                println(j + k)
                                                containsCoord = true
                                                validMove = false
                                                break
                                            }

                                        }
                                    }
                                    if (containsCoord) {
                                        x = initialX
                                        y = initialY
                                        while (pos.size > 0) {
                                            pos.removeAt(0)
                                        }
                                        if (rotated) {
                                            myRotate(size)
                                        }
                                        selected = false
                                        return
                                    }
                                }
                            }
                        }
                        println(name)
                        while (pos.size > 0) {
                            board.cells[pos[0].x * 10 + pos[0].y].shipName = ""
                            pos.removeAt(0)
                        }
                        if (rotated) {
                            //var myCoord = coordinate()
                            for (k in 0 until size) {
                                pos.add(Coordinate(i + k, j, name))
                                board.cells[(i + k) * 10 + j].shipName = name
                            }
                        } else {
                            for (k in 0 until size) {
                                pos.add(Coordinate(i, j + k,name))
                                board.cells[i * 10 + j + k].shipName = name
                            }
                        }
                        selected = false
                        return
                    }


                }
            }
            //if(!validMove){
            x = initialX
            y = initialY
            if(rotated) {
                myRotate(size)
            }
            // }


            selected = false
        }
    }
    var validMove = false
    /*fun drawShip(){
        Rectangle(x, y, myWidth, myHeight)
    }*/
    fun setPos(thisX:Double, thisY:Double){
        if(selected){
            x = thisX
            y = thisY
        }
    }
}
class Board (private val dimension: Int) {
    var destroyerRemaining = 2
    var cruiserRemaining = 3
    var submarineRemaining = 3
    var battleshipRemaining = 4
    var carrierRemaining = 5
    // list of all cells, as a 1D array
    val cells = List(dimension * dimension) { Coordinate(it % dimension, it / dimension, "") }
    fun aiPlaceShip(){
        var thisOrientation = Random.nextInt(0, 2)
        var itX: Int
        var itY :Int
        //Let 0 be default (Vertical)
        if(thisOrientation == 0){
            itX = Random.nextInt(0, 9)
            itY = Random.nextInt(0, 8)
            cells[itX * 10 + itY].shipName = "destroyer"
            cells[itX * 10 + itY + 1].shipName = "destroyer"
        }else{
            itX = Random.nextInt(0, 8)
            itY = Random.nextInt(0, 9)
            cells[itX * 10 + itY].shipName = "destroyer"
            cells[(itX+1) * 10 + itY].shipName = "destroyer"
        }
        while(true){
            thisOrientation = Random.nextInt(0, 2)
            if(thisOrientation == 0){
                itX = Random.nextInt(0, 9)
                itY = Random.nextInt(0, 7)
                if(cells[itX* 10 + itY].shipName == "" && cells[itX* 10 + itY+1].shipName == "" &&cells[itX* 10 + itY+2].shipName == ""){
                    cells[itX * 10 + itY].shipName = "cruiser"
                    cells[itX * 10 + itY + 1].shipName = "cruiser"
                    cells[itX * 10 + itY + 2].shipName = "cruiser"
                    break
                }
            }else{
                itX = Random.nextInt(0, 7)
                itY = Random.nextInt(0, 9)
                if(cells[itX* 10 + itY].shipName == "" && cells[(itX+1)* 10 + itY].shipName == "" &&cells[(itX+2)* 10 + itY].shipName == "") {
                    cells[itX * 10 + itY].shipName = "cruiser"
                    cells[(itX + 1) * 10 + itY].shipName = "cruiser"
                    cells[(itX + 2) * 10 + itY].shipName = "cruiser"
                    break
                }
            }
        }
        while(true){
            thisOrientation = Random.nextInt(0, 2)
            if(thisOrientation == 0){
                itX = Random.nextInt(0, 9)
                itY = Random.nextInt(0, 7)
                if(cells[itX* 10 + itY].shipName == "" && cells[itX* 10 + itY+1].shipName == "" &&cells[itX* 10 + itY+2].shipName == ""){
                    cells[itX * 10 + itY].shipName = "submarine"
                    cells[itX * 10 + itY + 1].shipName = "submarine"
                    cells[itX * 10 + itY + 2].shipName = "submarine"
                    break
                }
            }else{
                itX = Random.nextInt(0, 7)
                itY = Random.nextInt(0, 9)
                if(cells[itX* 10 + itY].shipName == "" && cells[(itX+1)* 10 + itY].shipName == "" &&cells[(itX+2)* 10 + itY].shipName == "") {
                    cells[itX * 10 + itY].shipName = "submarine"
                    cells[(itX + 1) * 10 + itY].shipName = "submarine"
                    cells[(itX + 2) * 10 + itY].shipName = "submarine"
                    break
                }
            }
        }
        while(true){
            thisOrientation = Random.nextInt(0, 2)
            if(thisOrientation == 0){
                itX = Random.nextInt(0, 9)
                itY = Random.nextInt(0, 6)
                if(cells[itX* 10 + itY].shipName == "" && cells[itX* 10 + itY+1].shipName == "" &&cells[itX* 10 + itY+2].shipName == ""&&cells[itX* 10 + itY+3].shipName == ""){
                    cells[itX * 10 + itY].shipName = "battleship"
                    cells[itX * 10 + itY + 1].shipName = "battleship"
                    cells[itX * 10 + itY + 2].shipName = "battleship"
                    cells[itX * 10 + itY + 3].shipName = "battleship"
                    break
                }
            }else{
                itX = Random.nextInt(0, 6)
                itY = Random.nextInt(0, 9)
                if(cells[itX* 10 + itY].shipName == "" && cells[(itX+1)* 10 + itY].shipName == "" &&cells[(itX+2)* 10 + itY].shipName == ""&&cells[(itX+3)* 10 + itY].shipName == "") {
                    cells[itX * 10 + itY].shipName = "battleship"
                    cells[(itX + 1) * 10 + itY].shipName = "battleship"
                    cells[(itX + 2) * 10 + itY].shipName = "battleship"
                    cells[(itX + 3) * 10 + itY].shipName = "battleship"
                    break
                }
            }
        }
        while(true){
            thisOrientation = Random.nextInt(0, 2)
            if(thisOrientation == 0){
                itX = Random.nextInt(0, 10)
                itY = Random.nextInt(0, 6)
                if(cells[itX* 10 + itY].shipName == "" && cells[itX* 10 + itY+1].shipName == "" &&cells[itX* 10 + itY+2].shipName == "" && cells[itX* 10 + itY+3].shipName == "" &&cells[itX* 10 + itY+4].shipName == ""){
                    cells[itX * 10 + itY].shipName = "carrier"
                    cells[itX * 10 + itY + 1].shipName = "carrier"
                    cells[itX * 10 + itY + 2].shipName = "carrier"
                    cells[itX * 10 + itY + 3].shipName = "carrier"
                    cells[itX * 10 + itY + 4].shipName = "carrier"
                    break
                }
            }else{
                itX = Random.nextInt(0, 6)
                itY = Random.nextInt(0, 10)
                if(cells[itX* 10 + itY].shipName == "" && cells[(itX+1)* 10 + itY].shipName == "" &&cells[(itX+2)* 10 + itY+2].shipName == "" && cells[(itX+3)* 10 + itY].shipName == "" &&cells[(itX+4)* 10 + itY].shipName == "") {
                    cells[itX * 10 + itY].shipName = "carrier"
                    cells[(itX + 1) * 10 + itY].shipName = "carrier"
                    cells[(itX + 2) * 10 + itY].shipName = "carrier"
                    cells[(itX + 3) * 10 + itY].shipName = "carrier"
                    cells[(itX + 4) * 10 + itY].shipName = "carrier"
                    break
                }
            }
        }
    }

    fun attackShip(itX: Int, itY: Int): Boolean {
        if (itX >= 10 || itX < 0 || itY >= 10 || itY < 0) {
            return false
        }
        if (cells[itX * 10 + itY].state != "water") {
            return false
        }
        if (cells[itX * 10 + itY].shipName == "destroyer") {
            destroyerRemaining -= 1
            cells[itX * 10 + itY].state = "hit"
            if (destroyerRemaining == 0) {
                for (i in 0 until 10) {
                    for (j in 0 until 10) {
                        if (cells[i * 10 + j].shipName == "destroyer") {
                            cells[i * 10 + j].state = "sunk"
                        }
                    }
                }
            }
        } else if (cells[itX * 10 + itY].shipName == "cruiser") {
            cruiserRemaining -= 1
            cells[itX * 10 + itY].state = "hit"
            if (cruiserRemaining == 0) {
                for (i in 0 until 10) {
                    for (j in 0 until 10) {
                        if (cells[i * 10 + j].shipName == "cruiser") {
                            cells[i * 10 + j].state = "sunk"
                        }
                    }
                }
            }
        } else if (cells[itX * 10 + itY].shipName == "submarine") {
            submarineRemaining -= 1
            cells[itX * 10 + itY].state = "hit"
            if (submarineRemaining == 0) {
                for (i in 0 until 10) {
                    for (j in 0 until 10) {
                        if (cells[i * 10 + j].shipName == "submarine") {
                            cells[i * 10 + j].state = "sunk"
                        }
                    }
                }
            }
        } else if (cells[itX * 10 + itY].shipName == "battleship") {
            battleshipRemaining -= 1
            cells[itX * 10 + itY].state = "hit"
            if (battleshipRemaining == 0) {
                for (i in 0 until 10) {
                    for (j in 0 until 10) {
                        if (cells[i * 10 + j].shipName == "battleship") {
                            cells[i * 10 + j].state = "sunk"
                        }
                    }
                }
            }
        } else if (cells[itX * 10 + itY].shipName == "carrier") {
            carrierRemaining -= 1
            cells[itX * 10 + itY].state = "hit"
            if (carrierRemaining == 0) {
                for (i in 0 until 10) {
                    for (j in 0 until 10) {
                        if (cells[i * 10 + j].shipName == "carrier") {
                            cells[i * 10 + j].state = "sunk"
                        }
                    }
                }
            }
        } else {
            cells[itX * 10 + itY].state = "attacked"
        }
        return true
    }
}
var board = Board(10)
var oppboard = Board(10)
var myText = "My Fleet"

class Battleship : Application() {
    override fun start(stage: Stage) {
        //val game = Game(10, true)
        //val player = UI(game)
        //val computer = AI(game)
        val destroyer = Ship()
        destroyer.myShip(2, 380.0, 50.0, "destroyer")
        //destroyer.size = 2
        //destroyer.myHeight = 27.5 * 2
        val cruiser= Ship()
        cruiser.myShip(3, 420.0, 50.0, "cruiser")
        //cruiser.size = 3
        //cruiser.myHeight = 27.5 * 3
        val submarine = Ship()
        submarine.myShip(3, 380.0, 150.0, "submarine")
        //submarine.size = 3
        //submarine.myHeight = 27.5 * 3
        val battleship = Ship()
        battleship.myShip(4, 420.0, 150.0, "battleship")
        val carrier = Ship()
        carrier.myShip(5, 460.0, 150.0, "carrier")
        //battleship.size = 4
        //battleship.myHeight = 27.5 * 4
        val myShips = mutableListOf(destroyer, cruiser, submarine, battleship, carrier)

        //game.startGame()
        val canvas = Canvas(875.0, 375.0)
        var xFactor = canvas.width / 875.0
        var yFactor = canvas.height / 375.0
        var minFactor = min(xFactor, yFactor)
        var prevminFactor = minFactor
        fun redraw() {
            canvas.graphicsContext2D.apply {
                /*for(i in 0 until myShips.size){
                    myShips[i].setPos((myShips[i].x - myShips[i].myWidth/2) * minFactor,(myShips[i].y-myShips[i].myHeight/2) * minFactor)
                }*/
                xFactor = canvas.width / 875.0
                yFactor = canvas.height / 375.0
                prevminFactor = minFactor
                minFactor = min(xFactor, yFactor)
                clearRect(0.0, 0.0, canvas.width, canvas.height)

                strokeText("My Formation", (875.0 / 3 - 150) * minFactor, 20.0 * minFactor)
                strokeText(myText, (875.0 / 2 - 30) * minFactor, 20.0 * minFactor)
                strokeText("Opponent's Waters", (875.0 * 2 / 3 + 60) * minFactor, 20.0 * minFactor)
                //Players Side board
                fill = Color.LIGHTBLUE
                //board.cells[99].state = "attacked"
                for (i in 0 until 10) {
                    strokeText((i + 1).toString(), (50 + 27.5 * i + 10) * minFactor, 40.0 * minFactor)
                    strokeText((i + 1).toString(), (50 + 27.5 * i + 10) * minFactor, (375 - 30.0) * minFactor)
                    strokeText((i + 65).toChar().toString(), 30.0 * minFactor, (50 + 27.5 * i + 20) * minFactor)
                    strokeText((i + 65).toChar().toString(), (375 - 35.0) * minFactor, (50 + 27.5 * i + 20) * minFactor)
                    for (j in 0 until 10) {
                        if(board.cells[i * 10 + j].state == "water" || (myText != "My Fleet" && board.cells[i * 10 + j].state != "sunk")){
                            fill = Color.LIGHTBLUE
                        }else if(board.cells[i * 10 + j].state == "attacked"){
                            fill = Color.LIGHTGRAY


                        }
                        else if(board.cells[i * 10 + j].state == "hit"){
                            fill = Color.CORAL
                        }
                        else if(board.cells[i * 10 + j].state == "sunk"){
                            fill = Color.DARKGRAY
                        }
                        fillRect((50 + 27.5 * i) * minFactor, (50.0 + 27.5 * j) * minFactor, (27.5) * minFactor, 27.5 * minFactor)
                    }
                }
                for (i in 0 until 11) {

                    strokeLine(50.0 * minFactor, (50.0 + 27.5 * i) * minFactor, 325.0 * minFactor, (50.0 + 27.5 * i) * minFactor)
                    strokeLine((50.0 + 27.5 * i) * minFactor, 325.0 * minFactor, (50.0 + 27.5 * i) * minFactor, 50.0 * minFactor)

                }

                //Opponents Side of Board
                fill = Color.LIGHTBLUE
                for (i in 0 until 10) {
                    strokeText((i + 1).toString(), (550 + 27.5 * i + 10) * minFactor, 40.0 * minFactor)
                    strokeText((i + 1).toString(), (550 + 27.5 * i + 10) * minFactor, (375 - 30.0) * minFactor)
                    strokeText((i + 65).toChar().toString(), (30 + 500.0) * minFactor, (50 + 27.5 * i + 20) * minFactor)
                    strokeText((i + 65).toChar().toString(), (875 - 35.0) * minFactor, (50 + 27.5 * i + 20) * minFactor)
                    for (j in 0 until 10) {
                        if(oppboard.cells[i * 10 + j].state == "water" || (myText != "My Fleet" && oppboard.cells[i * 10 + j].state != "sunk")){
                            fill = Color.LIGHTBLUE
                        }else if(oppboard.cells[i * 10 + j].state == "attacked"){
                            fill = Color.LIGHTGRAY


                        }
                        else if(oppboard.cells[i * 10 + j].state == "hit"){
                            fill = Color.CORAL
                        }
                        else if(oppboard.cells[i * 10 + j].state == "sunk"){
                            fill = Color.DARKGRAY
                        }
                        fillRect((550 + 27.5 * i) * minFactor, (50.0 + 27.5 * j) * minFactor, 27.5 * minFactor, 27.5 * minFactor)
                    }
                }
                for (i in 0 until 11) {

                    strokeLine(550.0 * minFactor, (50.0 + 27.5 * i) * minFactor, (500 + 325.0) * minFactor, (50.0 + 27.5 * i) * minFactor)
                    strokeLine((550.0 + 27.5 * i) * minFactor, 325.0* minFactor, (500 + 50.0 + 27.5 * i) * minFactor, 50.0 * minFactor)


                }
                val myColors = mutableListOf<Color>(Color.GREEN, Color.RED, Color.GOLD, Color.DARKSLATEGRAY,Color.BLACK)
                for(i in 0 until myShips.size){
                    fill=myColors[i]
                    val it = myShips[i]
                    fillRect((it.x ) * minFactor, (it.y ) * minFactor, (it.myWidth ) * minFactor, (it.myHeight ) * minFactor)
                }/*
                fill = Color.GREEN
                fillRect(destroyer.x, destroyer.y, destroyer.myWidth, destroyer.myHeight)
                fill = Color.RED
                fillRect(cruiser.x, cruiser.y, cruiser.myWidth, cruiser.myHeight)
                fill = Color.GOLD
                fillRect(submarine.x, submarine.y, submarine.myWidth, submarine.myHeight)
                fill = Color.DARKSLATEGRAY
                fillRect(battleship.x, battleship.y, battleship.myWidth, battleship.myHeight)
                fill = Color.BLACK
                fillRect(carrier.x, carrier.y, carrier.myWidth, carrier.myHeight)*/

            }
        }
        redraw()
        /*            var middle = VBox(Canvas(125.0, 375.0/3),Button("Start Game").apply{
                                                                                       translateX = 125.0/2-25
                    }, Button("End Game").apply{translateX=125.0/2-25}).apply {
                        prefWidth = 125.0
                    }*/

        /*var oppSide = Canvas(375.0, 375.0)
        oppSide.graphicsContext2D.apply {
            for (i in 0 until 11) {
                strokeLine(50.0, 50.0 + 27.5 * i, 325.0, 50.0 + 27.5 * i)
                strokeLine(50.0 + 27.5 * i, 325.0, 50.0 + 27.5 * i, 50.0)
            }
        }*/
        stage.apply {
            var gameStart = false
            val startButton = Button("Start Game").apply {
                setOnAction {
                    gameStart = true
                    isDisable = true
                    oppboard.aiPlaceShip()
                    println("HELLO WORLD")

                }
            }
            val endButton = Button("End Game").apply {
                /*setOnAction{
                    exitProcess(0)
                }*/
            }
            scene = Scene(AnchorPane(/*.apply{

                    AnchorPane.setLeftAnchor(this, 875.0/2 - 30)},*/canvas,startButton.apply {
                isDisable = true
                AnchorPane.setBottomAnchor(this, 60.0)
                AnchorPane.setLeftAnchor(this, 875.0/2 - 30)
            },endButton.apply {
                setOnAction{
                    exitProcess(0)
                }
                AnchorPane.setBottomAnchor(this, 30.0)
                AnchorPane.setLeftAnchor(this, 875.0/2-28)
            }), 875.0, 375.0).apply{
                widthProperty().addListener{_ ->
                    canvas.width = scene.width
                    xFactor = canvas.width / 875.0
                    yFactor = canvas.height / 375.0
                    minFactor = min(xFactor, yFactor)
                    startButton.apply{
                        AnchorPane.setBottomAnchor(this, 60.0 * minFactor)
                        AnchorPane.setLeftAnchor(this, (875.0/2 - 30) * minFactor)
                    }
                    endButton.apply{
                        AnchorPane.setBottomAnchor(this, 30.0 * minFactor)
                        AnchorPane.setLeftAnchor(this, (875.0/2-28) * minFactor)
                    }
                    redraw()
                }
                heightProperty().addListener{_ ->
                    canvas.height = scene.height
                    xFactor = canvas.width / 875.0
                    yFactor = canvas.height / 375.0
                    minFactor = min(xFactor, yFactor)
                    startButton.apply{
                        AnchorPane.setBottomAnchor(this, 60.0 * minFactor)
                        AnchorPane.setLeftAnchor(this, (875.0/2 - 30) * minFactor)
                    }
                    endButton.apply{
                        AnchorPane.setBottomAnchor(this, 30.0 * minFactor)
                        AnchorPane.setLeftAnchor(this, (875.0/2-28) * minFactor)
                    }
                    redraw()
                }
                //Property
            }
            val myList = mutableListOf<Coordinate>()
            for(i in 0 until 10){
                for(j in 0 until 10){
                    myList.add(Coordinate(i,j,""))
                }
            }
            scene.addEventHandler(MouseEvent.MOUSE_CLICKED){
                var gameEnd = false
                if(it.button == MouseButton.PRIMARY /*&& !gameEnd*/) {
                    if(!gameStart) {
                        val notSelected =
                            !carrier.selected && !battleship.selected && !cruiser.selected && !submarine.selected && !destroyer.selected
                        for (i in 0 until myShips.size) {
                            myShips[i].placeShip(it.x / minFactor, it.y/ minFactor, notSelected, myShips)
                            myShips[i].setPos(it.x/ minFactor - myShips[i].myWidth/2,it.y/ minFactor-myShips[i].myHeight/2)
                        }

                        startButton.isDisable = true
                    }
                    val allShipsPlaced = carrier.validMove && battleship.validMove && cruiser.validMove && submarine.validMove && destroyer.validMove

                    if(allShipsPlaced){
                        startButton.isDisable = false
                    }
                    if(gameStart){
                        startButton.isDisable = true
                        if(oppboard.attackShip(myFloor((it.x/ minFactor - 550.0)/27.5), myFloor((it.y/ minFactor - 50.0)/27.5))){
                            val myCoord = Random.nextInt(myList.size)


                            if(oppboard.destroyerRemaining == 0 && oppboard.cruiserRemaining == 0 && oppboard.submarineRemaining == 0 && oppboard.battleshipRemaining == 0 && oppboard.carrierRemaining == 0){
                                myText = "You won!"
                                gameEnd = true

                            }
                            if(!gameEnd){
                                board.attackShip(myList[myCoord].x, myList[myCoord].y)
                                myList.removeAt(myCoord)
                            }

                            if(board.destroyerRemaining == 0 && board.cruiserRemaining == 0 && board.submarineRemaining == 0 && board.battleshipRemaining == 0 && board.carrierRemaining == 0){
                                myText = "You were defeated!"
                                gameEnd = true


                            }
                            if(gameEnd){
                                if(board.destroyerRemaining>0){
                                    myShips[0].x = myShips[0].initialX
                                    myShips[0].y = myShips[0].initialY
                                    if(myShips[0].rotated){
                                        myShips[0].selected = true
                                        myShips[0].myRotate(myShips[0].size)
                                        myShips[0].selected = false
                                    }
                                }
                                if(board.cruiserRemaining>0){
                                    myShips[1].x = myShips[1].initialX
                                    myShips[1].y = myShips[1].initialY
                                    if(myShips[1].rotated){
                                        myShips[1].selected = true
                                        myShips[1].myRotate(myShips[1].size)
                                        myShips[1].selected = false
                                    }
                                }
                                if(board.submarineRemaining>0){
                                    myShips[2].x = myShips[2].initialX
                                    myShips[2].y = myShips[2].initialY
                                    if(myShips[2].rotated){
                                        myShips[2].selected = true
                                        myShips[2].myRotate(myShips[2].size)
                                        myShips[2].selected = false
                                    }
                                }
                                if(board.battleshipRemaining>0){
                                    myShips[3].x = myShips[3].initialX
                                    myShips[3].y = myShips[3].initialY
                                    if(myShips[3].rotated){
                                        myShips[3].selected = true
                                        myShips[3].myRotate(myShips[3].size)
                                        myShips[3].selected = false
                                    }
                                }
                                if(board.carrierRemaining>0){
                                    myShips[4].x = myShips[4].initialX
                                    myShips[4].y = myShips[4].initialY
                                    if(myShips[4].rotated){
                                        myShips[4].selected = true
                                        myShips[4].myRotate(myShips[4].size)
                                        myShips[4].selected = false
                                    }
                                }
                            }

                        }

                    }

                }else if(it.button== MouseButton.SECONDARY){
                    for(i in 0 until myShips.size){
                        myShips[i].myRotate(myShips[i].size)
                        myShips[i].setPos((it.x/ minFactor - myShips[i].myWidth/2) ,(it.y / minFactor-myShips[i].myHeight/2))
                    }
                }
                //if(it.)

                redraw()
            }
            scene.addEventHandler(MouseEvent.MOUSE_MOVED){
                destroyer.setPos((it.x/ minFactor - destroyer.myWidth/2) ,(it.y/ minFactor-destroyer.myHeight/2) )
                cruiser.setPos((it.x/ minFactor - cruiser.myWidth/2) ,(it.y/ minFactor - cruiser.myHeight / 2) )
                submarine.setPos((it.x/ minFactor - submarine.myWidth/2) ,(it.y/ minFactor - submarine.myHeight / 2) )
                battleship.setPos((it.x/ minFactor - battleship.myWidth / 2) , (it.y/ minFactor - battleship.myHeight / 2) )
                carrier.setPos((it.x/ minFactor - carrier.myWidth / 2) ,(it.y/ minFactor - carrier.myHeight / 2) )

                redraw()
            }
            title = "CS349 - A3 Battleship - ksaidoun"
        }.show()
    }
}

fun main(){

    Battleship()
}