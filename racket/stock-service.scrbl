
#lang scribble/manual
 
@title{stock-service}
@author{author+email "" ""}

@section{Structs}
 
Get the latest information about stocks.


@defproc[(make-stock [id number?]
			[ticker string?]
			[exchange string?]
			[last number?]
			[last-trade-date string?]
			[last-trade-time string?]
			[change number?]
			[percent-change number?]) stock]{

A structured representation of stock information, including ticker symbol, latest sale price, and price change since yesterday.
@itemlist[

			@item{@racket[id] --- The unique ID number for this ticker symbol}

			@item{@racket[ticker] --- The Ticker Symbol (e.g. AAPL)}

			@item{@racket[exchange] --- The name of the exchange (e.g. NASDAQ)}

			@item{@racket[last] --- The latest sale price for this stock.}

			@item{@racket[last-trade-date] --- The entire date of the last trade.}

			@item{@racket[last-trade-time] --- The time of the last trade.}

			@item{@racket[change] --- The price change since yesterday.}

			@item{@racket[percent-change] --- The percent price change since yesterday.}]}



@section{Functions}

@defproc[(disconnect-stock-service ) void]{

Establishes that data will be retrieved locally.
@itemlist[

		]}

@defproc[(connect-stock-service ) void]{

Establishes that the online service will be used.
@itemlist[

		]}

@defproc[(get-stock-information [ticker string?]) (listof stock?)]{

Retrieves current stock information.
@itemlist[

			@item{@racket[ticker] --- A comma separated list of ticker symbols (e.g. "AAPL, MSFT, CSCO").}]}

