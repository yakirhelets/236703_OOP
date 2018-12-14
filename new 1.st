compile: aSourceCode where: anOrderedCollection
| s stream reg1 reg2 sFirstLine sSecLine sSecLine2 sInjected firstCodeLine secCodeLine sCode args |

stream := aSourceCode readStream.
sFirstLine := String new.
sFirstLine := stream nextLine. "first line - function name+ parameters"
sSecLine := String new.
sSecLine := stream nextLine. "second line - maybe local variables maybe firstline of code"
sSecLine2 := String new.

"creating the local variables line"
reg1:= '.*\|.*' asRegex.
(reg1 matches: sSecLine) "we already have local vars"
	ifTrue: [
		sSecLine2 := '|', (sSecLine copyWithout: $| ), 'args restricts index |'.
		sCode := (stream nextLine),Character cr.
		[stream atEnd] whileFalse: [sCode := sCode, (stream nextLine),Character cr].
	]
	ifFalse: [sSecLine2 := '| args restricts |'.
			sCode := sSecLine.
			[stream atEnd] whileFalse: [sCode := sCode, (stream nextLine),Character cr].
	].

"getting the args from the function parameters"
args := OrderedCollection new.
reg1:= '\:(\s)*(\w)+' .
reg2 := '\w+' .
sFirstLine copyWithRegex: reg1 matchesTranslatedUsing: [:match |
	match copyWithRegex: reg2 matchesTranslatedUsing: [:arg | args add: arg]].
	
"( sFirstLine =~ reg1 ) ifMatched: [:match |
	( match =~ '(^:\s)' ) ifMatched: [: arg |
	args add: arg].]."
	

"first code line - args init"
firstCodeLine := String new.
firstCodeLine := 'args := OrderedCollection new. ', Character cr,
	'args := #('.
args do: [:arg | firstCodeLine := firstCodeLine,arg,' '].
firstCodeLine := firstCodeLine,').'.
	
"second code line - restricts init"
secCodeLine := String new.
secCodeLine := 'restricts := OrderedCollection new. ', Character cr,
	'restricts :=#('.
anOrderedCollection do: [:arg | secCodeLine := secCodeLine, arg,' '].
secCodeLine := secCodeLine, ').'.


s:= String new.
sInjected:= String new.

(args size == anOrderedCollection size) 
	ifFalse:[self error: 'Can not compile method, number of arguments is not equal to the number of constraints!'].


"filling up args"
sInjected := 'index := 1.',Character cr.
sInjected := sInjected, 'restricts do: [:rest | 
				((rest asString) = ''nil'')
				ifFalse:[ 
						(  (args at: index)  isKindOf: (Smalltalk classNamed: (rest )) ) "we got a problem here with args at:index #a instaead of var a"
						ifFalse: [self error: ''Type mismatch! Argument '', (index asString) , '' should be '', (rest asString)].
						].
				index := index + 1.	
					].'.


s:= sFirstLine,Character cr, sSecLine2,Character cr,
	firstCodeLine, Character cr, secCodeLine, Character cr,
	sInjected ,Character cr, sCode.
	"Transcript show: s. REMOVEE!!!"
self class compile: s.