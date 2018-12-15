compile: aSourceCode where: anOrderedCollection
| s stream reg1 reg2 sFirstLine sSecLine sSecLine2 sInjected sCode args typeCheck |

stream := aSourceCode readStream.
sFirstLine := String new.
sFirstLine := stream nextLine. "first line - function name+ parameters"
sSecLine := String new.
sSecLine := stream nextLine. "second line - maybe local variables maybe firstline of code"


"creating the local variables line"
sSecLine2 := String new.
reg1:= '.*\|.*' asRegex.
(reg1 matches: sSecLine) "we already have local vars"
	ifTrue: [
		sSecLine2 := '|', (sSecLine copyWithout: $| ), ' index currentClass |'.
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

"check the size of the restriction and the arguments given"	
(args size == anOrderedCollection size) 
	ifFalse:[self error: 'Can not compile method, number of arguments is not equal to the number of constraints!'].

"here we know the sizes are equal"
typeCheck := String new.
typeCheck := '((true) '.
(args size >= 1)
ifTrue: [
	((anOrderedCollection at:1) asString = 'nil')
	ifFalse: [
	typeCheck :=typeCheck, 'and:[currentClass:=''',(anOrderedCollection at:1),'''. ',(args at:1),' isKindOf: (',(anOrderedCollection at:1),')] '.]]. "first"
2 to: (args size) do:
	[:i | 
			((anOrderedCollection at:i) asString = 'nil')
			ifTrue: [typeCheck :=typeCheck, 'and:[index:= index+1. true] ']
			ifFalse: [
			typeCheck := typeCheck,'and:[index:= index+1. currentClass:=''',(anOrderedCollection at:i) ,'''. ',
			(args at:i),' isKindOf: (',(anOrderedCollection at:i),')] '].].
typeCheck := typeCheck,')',Character cr.
"now typeCheck is ((XXX) and:[XXX]"

s:= String new.
sInjected:= String new.

"creating injected code for checking"
sInjected := 'index := 1.',Character cr.
sInjected := sInjected, 'currentClass := String new.',Character cr.
sInjected := sInjected,typeCheck,
						'ifFalse: [self error: ''Type mismatch! Argument '', (index asString) , '' should be '', currentClass ].'.


s:= sFirstLine,Character cr, sSecLine2,Character cr,
	sInjected ,Character cr, sCode.
	"Transcript show: s. REMOVEE!!!"
self class compile: s.