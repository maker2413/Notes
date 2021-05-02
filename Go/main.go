package main

import "fmt"

// Name of the game we will play
var game string

// Array of our 5 tables
var tablelist [5]int

// Slice for our deck
var dark []string

func main() {
	cards := []string{"Ace of Diamonds", newCard()}
	cards = append(cards, "Six of Spade")

	for index, card := range cards {
		fmt.Println(index, card)
	}

	game="BlackJack"
	fmt.Println(game)

	for i := range tablelist {
		tablelist[i]=i + 1
	}

	fmt.Println(tablelist)
}

func newCard() string {
	return "Five of Diamonds"
}
