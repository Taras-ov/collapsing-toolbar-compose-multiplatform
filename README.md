# collapsing-toolbar-compose-multiplatform
Hey everyone! Here you’ll find a guide on how to implement a collapsing toolbar in Compose Multiplatform without using third-party libraries. Check out the README for details!

You can use this toolbar for your own dimensions—just replace them with the appropriate values. Also, keep in mind that the animation speed formulas are tailored for the example size (60.dp).

Also, keep in mind that you don’t have to use the gradual content shrinking animation for the toolbar—you can simply adjust the alpha instead. Don't forget to pass the alpha to the modifier of the toolbar components you want to fade out gradually.

On test devices, it may seem like the toolbar behaves incorrectly when releasing the scroll. This is likely due to the scroll sensitivity on emulators or touchpads. On physical devices, everything works correctly! 

If you’d like to report a bug or have ideas on how to improve it, I’d be happy to discuss them with you!
