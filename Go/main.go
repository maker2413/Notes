package main

import "fmt"

func main() {
	cards := []string{"Ace of Diamonds", newCard()}
	cards = append(cards, "Six of Spade")

	for index, card := range cards {
		fmt.Println(index, card)
	}
}

func newCard() string {
	return "Five of Diamonds"
}
